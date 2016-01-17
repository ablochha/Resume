package com.team23.weather;

import com.team23.weather.configuration.Configuration;
import com.team23.weather.configuration.IConfigurationManager;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Location;
import com.team23.weather.models.WeatherData;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.ILocationObserver;
import com.team23.weather.observer.IWeatherDataObserver;
import com.team23.weather.owm.IOwmClient;
import com.team23.weather.mars.IMarsWeatherClient;
import com.team23.weather.errorhandling.*;
import com.team23.weather.errorhandling.Error;

import java.util.ArrayList;

/**
 * This class represents the main backend of the application.
 * It acts as a singleton and is largely not-threadsafe, unless indicated.
 *
 * A reference to this object is given to every top-level UI element,
 * so that it can communicate with the backend.
 *
 * @author Saquib Mian
 */
public class App {

    /**
     * A thread-synchronization lock for loading the weather
     */
    private final Object _weatherLoadingLock = new Object();

    /**
     * A thread-synchronization lock for loading the configuration
     */
    private final Object _configLoadingLock = new Object();

    /**
     * the configuration manager
     */
    private final IConfigurationManager _configurationManager;

    /**
     * the OWM client
     */
    private final IOwmClient _owmClient;

    /**
     * the mars weather client
     */
    private final IMarsWeatherClient _marsWeatherClient;

    /**
     * cached (already loaded) weather data
     * this field is only updated on a force refresh
     */
    private WeatherData _weatherData = null;

    /**
     * cached (already loaded) configuration
     * this field is only updated on a configuration update
     */
    private Configuration _configuration = null;

    /**
     * registered locale observers, to be notified of locale changes
     */
    private final ArrayList<ILocaleObserver> _localeObservers = new ArrayList<ILocaleObserver>();

    /**
     * registered location observers, to be notified of location changes
     */
    private final ArrayList<ILocationObserver> _locationObservers = new ArrayList<ILocationObserver>();

    /**
     * registered weather data observers, to be notified of weather data changes
     */
    private final ArrayList<IWeatherDataObserver> _weatherDataObservers = new ArrayList<IWeatherDataObserver>();

    /**
     * registered error handlers, to be executed when errors occur
     */
    private final ArrayList<IErrorHandler> _errorHandlers = new ArrayList<IErrorHandler>();

    public App(IConfigurationManager configurationManager, IOwmClient owmClient, IMarsWeatherClient marsWeatherClient) {
        _configurationManager = configurationManager;
        _owmClient = owmClient;
        _marsWeatherClient = marsWeatherClient;
        _weatherData = new WeatherData();
    }

    /**
     * Register a new locale observer. This observer will be triggered on locale changes.
     * @param lo the observer
     */
    public void registerObserver(ILocaleObserver lo) {
        _localeObservers.add(lo);
    }

    /**
     * Register a new location observer. This observer will be triggered on location changes.
     * @param lo the observer
     */
    public void registerObserver(ILocationObserver lo) {
        _locationObservers.add(lo);
    }

    /**
     * Register a new weather data observer. This observer will be triggered on weather data changes.
     * @param wdo the observer
     */
    public void registerObserver(IWeatherDataObserver wdo) {
        _weatherDataObservers.add(wdo);
    }

    /**
     * Register a new error handler. This handler will be triggered when errors occur.
     * @param eh the error handler
     */
    public void registerErrorHandler(IErrorHandler eh) {
        _errorHandlers.add(eh);
    }

    /**
     * Searches for a city in OWM and returns an array of results.
     *
     * This method is thread-safe.
     *
     * @param query the name of the city and/or country, like so: 'city,country'
     * @return an array of {@link com.team23.weather.models.Location} results
     */
    public Location[] searchForCity(String query) {
        try {
            return _owmClient.searchForCity(query);
        } catch (Exception e) {
            Error error = new Error("Error getting data from OWM; please try again", _owmClient, e);
            for(IErrorHandler eh : _errorHandlers) {
                eh.handleError(error);
            }
        }

        return null;
    }

    /**
     * Load the weather data, if it is not already loaded.
     * Note that if a location has not been chosen as yet, this method will return a
     * valid {@link com.team23.weather.models.WeatherData} object with no location-specific weather data.
     *
     * This method is thread-safe.
     *
     * @return a {@link com.team23.weather.models.WeatherData} object
     */
    public WeatherData getWeatherData() {
        synchronized(_weatherLoadingLock) {
            Location location = getConfiguration().getCurrentLocation();
            if (!_weatherData.hasLocationWeatherData() && location != null) {
                refreshWeatherData(location);
            }

            return _weatherData;
        }
    }

    /**
     * Force a refresh of all weather data, including Mars data.
     * This method will fire any relevant observers.
     * Note that this method will only update Mars data if no location is set.
     *
     * @param location the location to get the weather for
     */
    public void refreshWeatherData(Location location) {
        for( IWeatherDataObserver wdo : _weatherDataObservers) {
            wdo.beforeWeatherDataUpdate(_weatherData);
        }

        _weatherData.setMarsWeather(_marsWeatherClient.getWeather());

        if( location == null ) {
            System.out.println("No location set!");
            return;
        } else {
            try {
                _weatherData.setWeather(_owmClient.getWeather(location));
                _weatherData.setHourlyForecast(_owmClient.getForecast(location));
                _weatherData.setDailyForecast(_owmClient.getDailyForecast(location));
            } catch (Exception e) {
                Error error = new Error("Error getting data from OWM; please try again", _owmClient, e);
                for(IErrorHandler eh : _errorHandlers) {
                    eh.handleError(error);
                }
            }
        }

        for( IWeatherDataObserver wdo : _weatherDataObservers) {
            wdo.onWeatherDataUpdated(_weatherData);
        }
    }


    /**
     * Sets the location and updates any weather, and persists the change.
     * Any {@link com.team23.weather.observer.ILocationObserver}s are fired.
     *
     * This method is thread-safe.
     *
     * @param location the new location
     */
    public void saveLocation(Location location) {
        Configuration toSave = getConfiguration();
        Location previousLocation = toSave.getCurrentLocation();
        toSave.setCurrentLocation(location);

        saveConfiguration(toSave);

        for( ILocationObserver obs : _locationObservers) {
            obs.beforeLocationChange(previousLocation);
        }
        synchronized(_weatherLoadingLock) {
            refreshWeatherData(toSave.getCurrentLocation());
        }
        for( ILocationObserver obs : _locationObservers) {
            obs.onLocationChange(toSave.getCurrentLocation());
        }
    }

    /**
     * Sets the locale settings and fires any {@link com.team23.weather.observer.ILocaleObserver}s.
     *
     * This method is thread-safe.
     *
     * @param localeSettings the new locale settings
     */
    public void setLocaleSettings(LocaleSettings localeSettings) {
        Configuration toSave = getConfiguration();
        toSave.setLocaleSettings(localeSettings);

        saveConfiguration(toSave);

        for( ILocaleObserver obs : _localeObservers) {
            obs.onLocaleChange(toSave.getLocaleSettings());
        }
    }

    /**
     * Persist the user preferences
     *
     * This method is thread-safe.
     *
     * @param toSave the new configuration model
     */
    private void saveConfiguration( Configuration toSave ) {
        synchronized (_configLoadingLock) {
            _configuration = toSave;
            _configurationManager.save(_configuration);
        }
    }

    /**
     * Reads the user preferences
     *
     * This method is thread-safe.
     *
     * @return the persisted configuration model
     */
    public Configuration getConfiguration() {
        synchronized (_configLoadingLock) {
            if( _configuration == null ) {
                _configuration = _configurationManager.load();
            }

            return _configuration;
        }
    }

}
