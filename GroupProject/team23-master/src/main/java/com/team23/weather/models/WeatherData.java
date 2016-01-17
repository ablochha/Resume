package com.team23.weather.models;

import java.util.Date;

/**
 * This model represents an aggregated weather model, to store
 * all of the data acquired during a single refresh event.
 *
 * @author Saquib Mian
 * @see com.team23.weather.models.Forecast
 * @see com.team23.weather.models.CompoundForecast
 * @see com.team23.weather.models.MarsForecast
 */
public class WeatherData {

    /**
     * the current weather
     */
    private Forecast _weather;

    /**
     * the hourly forecast
     */
    private CompoundForecast _hourlyForecast;

    /**
     * the daily forecast
     */
    private CompoundForecast _dailyForecast;

    /**
     * the mars forecast
     */
    private MarsForecast _marsWeather;

    /**
     * the last updated time
     */
    private Date _lastUpdateTime = null;

    public WeatherData() {
        _lastUpdateTime = new Date();
    }

    /**
     * get the current weather
     * @return the current weather
     */
    public Forecast getWeather() {
        return _weather;
    }

    /**
     * set the current weather
     * @param weather the current weather
     */
    public void setWeather(Forecast weather) {
        _lastUpdateTime = new Date();
        _weather = weather;
    }

    /**
     * get the hourly forecast
     * @return the hourly forecast
     */
    public CompoundForecast getHourlyForecast() {
        return _hourlyForecast;
    }

    /**
     * set the hourly forecast
     * @param hourlyForecast the hourly forecast
     */
    public void setHourlyForecast(CompoundForecast hourlyForecast) {
        _lastUpdateTime = new Date();
        _hourlyForecast = hourlyForecast;
    }

    /**
     * get the daily forecast
     * @return the daily forecast
     */
    public CompoundForecast getDailyForecast() {
        return _dailyForecast;
    }

    /**
     * set the daily forecast
     * @param dailyForecast the daily forecast
     */
    public void setDailyForecast(CompoundForecast dailyForecast) {
        _lastUpdateTime = new Date();
        _dailyForecast = dailyForecast;
    }

    /**
     * get the mars forecast
     * @return the mars forecast
     */
    public MarsForecast getMarsWeather() {
        return _marsWeather;
    }

    /**
     * set the mars forecast
     * @param marsWeather the mars forecast
     */
    public void setMarsWeather(MarsForecast marsWeather) {
        _lastUpdateTime = new Date();
        _marsWeather = marsWeather;
    }

    /**
     * get the last update time
     * @return the last update time
     */
    public Date getLastUpdateTime() {
        return _lastUpdateTime;
    }

    /**
     * @return true if this {@link com.team23.weather.models.WeatherData} has valid weather data for the current location
     */
    public boolean hasLocationWeatherData() {
        return  _weather != null &&
                _dailyForecast != null &&
                _hourlyForecast != null;
    }

    /**
     * @return true if this {@link com.team23.weather.models.WeatherData} has valid mars weather data
     */
    public boolean hasMarsWeatherData() {
        return  _marsWeather != null;
    }
}
