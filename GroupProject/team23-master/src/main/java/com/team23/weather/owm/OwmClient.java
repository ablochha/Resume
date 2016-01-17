package com.team23.weather.owm;

import com.team23.weather.http.HttpClient;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.CompoundForecast;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.models.SkyCondition;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.owm.dto.*;
import com.team23.weather.serialization.ISerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An OwmClient that implements the
 * {@link com.team23.weather.owm.IOwmClient} interface.
 *
 * This implementation of OwmClient also implements the
 * {@link com.team23.weather.observer.ILocaleObserver}
 * interface, to listen for locale changes.
 *
 * @see com.team23.weather.owm.IOwmClient
 * @see com.team23.weather.observer.ILocaleObserver
 * @author Saquib Mian
 */
public class OwmClient implements IOwmClient, ILocaleObserver{
    static Logger logger = LogManager.getLogger(OwmClient.class.getName());

    /**
     * the API key
     */
    private String _apiKey;

    /**
     * the base URL of the client
     */
    private String _baseUrl;

    /**
     * the locale to request from the API
     */
    private LocaleSettings _locale;

    /**
     * The {@link com.team23.weather.http.HttpClient} to make web requests with
     */
    private final HttpClient _httpClient;

    /**
     * The {@link com.team23.weather.serialization.ISerializer} to convert the web responses to objects
     */
    private final ISerializer _serializer;

    /**
     * any calls to getIcon are cached in this HashMap such that subsequent calls are fast
     */
    private final HashMap<String,Image> _cachedIcons = new HashMap<String, Image>();

    /**
     * A thread-synchronization lock used when interacting with the icon cache
     */
    private final Object _iconLoadingLock = new Object();

    public OwmClient(HttpClient httpClient, ISerializer serializer) {
        _httpClient = httpClient;
        _serializer = serializer;

        _baseUrl = "http://api.openweathermap.org/data/2.5";
        _apiKey = "62f1b30bf3ca9cff6853c61fda9e971b";

        /**
         * Uncomment the line below to re-route any API queries to a local REST mimic.
         * This is useful for when developing offline, or when the API is down.
         */
        //_baseUrl = "http://localhost:4730/data/2.5";
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public String getApiKey() {
        return _apiKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApiKey(String apiKey) {
        _apiKey = apiKey;
    }

    /**
     * {@inheritDoc}
     *
     * By default this URL is set to "http://api.openweathermap.org/data/2.5"
     */
    @Override
    public String getBaseUrl() {
        return _baseUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBaseUrl(String baseUrl) {
        _baseUrl = baseUrl;
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public LocaleSettings getLocale() {
        return _locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocale(LocaleSettings localeSettings) {
        _locale = localeSettings;
    }

    private String buildUrl(String api, String query) {
        String toReturn =  new StringBuilder()
                .append(_baseUrl)
                .append("/" + api + "/?")
                .append(query)
                .append("&units=" + _locale.getName().toLowerCase())
                .append("&APPID=" + _apiKey)
                .toString();
        return toReturn;
    }

    /**
     * Returns a URL-style query for a given location.
     *
     * This command will return with a query for the
     * location using the ID if the IS property is valid;
     * otherwise it will default to the name of the City and Country.
     *
     * @param location the location for which to generate the query
     * @return the query string component
     */
    private String getQuery(Location location) {
        String query = null;

        if( location.getLocationId() > 0 ) {
            query = "id=" + location.getLocationId();
        } else {
            query = "q=" + location.getCity();
            if( location.getCountry()!= null ) {
                query += "," + location.getCountry();
            }
        }

        return query;
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public Location[] searchForCity(String query) throws IOException {
        if( query == null || query.isEmpty() ) {
            throw new IllegalArgumentException("query is null or empty");
        }

        String path = buildUrl("find", "q=" + query) + "&type=like";
        String jsonResponse;

        jsonResponse = _httpClient.getAsString(path);

        SearchResponse response = _serializer.deserialize(jsonResponse, SearchResponse.class);
        ArrayList<Location> toReturn = new ArrayList<Location>();
        for (SearchResponseItem item : response.getList()) {
            Location toAdd = item.getLocation();
            Forecast forecast = item.getForecast( _locale.getUnits());
            forecast.getSkyCondition().setIconUrl(getIconUrl(forecast.getSkyCondition()));
            toAdd.setWeather(forecast);
            toReturn.add(toAdd);
        }

        return toReturn.toArray(new Location[toReturn.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Forecast getWeather(Location location) throws IOException {
        String path = buildUrl("weather", getQuery(location));
        String jsonResponse;

        jsonResponse = _httpClient.getAsString(path);

        WeatherResponse response = _serializer.deserialize(jsonResponse, WeatherResponse.class);

        Forecast toReturn = response.getForecast(_locale.getUnits());
        toReturn.getSkyCondition().setIconUrl(getIconUrl(toReturn.getSkyCondition()));

        return toReturn;
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public CompoundForecast getForecast(Location location) throws IOException {
        String path = buildUrl("forecast", getQuery(location));
        String jsonResponse;

        jsonResponse = _httpClient.getAsString(path);

        HourlyForecastResponse response = _serializer.deserialize(jsonResponse, HourlyForecastResponse.class);

        ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
        for (HourlyForecastResponseItem item : response.getList()) {
            Forecast forecast = item.getForecast( _locale.getUnits());
            forecast.setLocationInformation( location);
            forecast.getSkyCondition().setIconUrl(getIconUrl(forecast.getSkyCondition()));
            forecasts.add(forecast);
        }

        return new CompoundForecast(forecasts.toArray(new Forecast[forecasts.size()]), true, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompoundForecast getDailyForecast(Location location) throws IOException {
        String path = buildUrl("forecast/daily", getQuery(location));
        String jsonResponse;

        jsonResponse = _httpClient.getAsString(path);

        DailyForecastResponse response = _serializer.deserialize(jsonResponse, DailyForecastResponse.class);

        ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
        for (DailyForecastResponseItem item : response.getList()) {
            Forecast forecast = item.getForecast( _locale.getUnits());
            forecast.setLocationInformation( location);
            forecast.getSkyCondition().setIconUrl(getIconUrl(forecast.getSkyCondition()));
            forecasts.add(forecast);
        }

        return new CompoundForecast(forecasts.toArray(new Forecast[forecasts.size()]), false, true);
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public Image getIcon(SkyCondition skyCondition) throws IOException {
        String imageUrl = getIconUrl(skyCondition);
        synchronized (_iconLoadingLock) {
            if( !_cachedIcons.containsKey(imageUrl)) {
                _cachedIcons.put(imageUrl, ImageIO.read(_httpClient.get(imageUrl)));
            }
        }

        return _cachedIcons.get(imageUrl);
    }

    /**
     * {@inheritDoc}
     */
    public String getIconUrl(SkyCondition skyCondition) {
        return "http://openweathermap.org/img/w/" + skyCondition.getIconFileName() + ".png";
    }

    /**
     * When the locale changes, this will ensure that the client requests the correct units on the next API call
     * @param localeSettings the new locale settings
     */
    @Override
    public void onLocaleChange(LocaleSettings localeSettings) {
        setLocale(localeSettings);
    }
}
