package com.team23.weather.owm;

import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.CompoundForecast;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.models.SkyCondition;

import java.awt.*;
import java.io.IOException;

/**
 * An IOwmClient is the primary client layer interface
 * through which an application can query for data from the
 * OpenWeatherMap API.
 *
 * @see com.team23.weather.models.Forecast
 * @see com.team23.weather.models.CompoundForecast
 * @see com.team23.weather.models.Location
 * @author Saquib Mian
 */
public interface IOwmClient {

    /**
     * @return the API key in use by the client
     */
    public String getApiKey();

    /**
     * Set the API key to be used by the library
     * @param apiKey the new API key
     */
    public void setApiKey(String apiKey);

    /**
     *
     * By default this URL is set to "http://api.openweathermap.org/data/2.5"
     * @return the base URL of of this client library
     */
    public String getBaseUrl();

    /**
     * Sets the base URL to which all subsequent API calls are made
     * @param baseUrl the new base URL
     */
    public void setBaseUrl(String baseUrl);

    /**
     * @return the current {@link com.team23.weather.locale.LocaleSettings} object associated with this library
     */
    public LocaleSettings getLocale();

    /**
     * Sets the {@link com.team23.weather.locale.LocaleSettings} object associated with this library
     * @param localeSettings the new locale settings
     */
    public void setLocale(LocaleSettings localeSettings);

    /**
     * Search for the City given a query string. This query
     * string is usually in the format: "city,country".
     *
     * @param query the query
     * @throws IOException is the query is empty or null
     * @return a list of resulting {@link com.team23.weather.models.Location} object
     */
    Location[] searchForCity(String query) throws IOException;

    /**
     * Get the weather for the location specified.
     *
     * @param location the location to get the weather for
     * @throws IOException if connection fails
     * @return {@link com.team23.weather.models.Forecast} with as much data populated as the API returns
     */
    Forecast getWeather(Location location) throws IOException;

    /**
     * Get the short-term (hourly) forecast for the given location.
     * This forecast will be returned according to this implementation
     * defined in the OpenWeatherMap API, which at the time of this
     * writing is 3-hour segments for 24-hours.
     *
     * @param location the location to get the hourly forecasts for
     * @throws IOException if the connection failed
     * @return a {@link com.team23.weather.models.CompoundForecast} object with the appropriate data filed
     */
    CompoundForecast getForecast(Location location) throws IOException;

    /**
     * Get the long-term (daily) forecast for the given location.
     * This forecast will be returned according to this implementation
     * defined in the OpenWeatherMap API, which at the time of this
     * writing is 24-hour segments for 15 days.
     *
     * @param location the location to get the weather for
     * @throws IOException if connection fails
     * @return a {@link com.team23.weather.models.CompoundForecast} object with the appropriate data filed
     */
    CompoundForecast getDailyForecast(Location location) throws IOException;

    /**
     * Gets an {@link java.awt.Image} object representing the image of the sky condition
     *
     * @param skyCondition the sky condition for which to get the image
     * @throws IOException if connection fails
     * @return an {@link java.awt.Image} object representing the image of the sky condition
     */
    Image getIcon(SkyCondition skyCondition) throws IOException;

    /**
     * Gets the full URL of the icon that indicates the {@link com.team23.weather.models.SkyCondition}
     * @param skyCondition the sky condition for which to get the image url
     * @return the full URL of the icon
     */
    String getIconUrl(SkyCondition skyCondition);
}
