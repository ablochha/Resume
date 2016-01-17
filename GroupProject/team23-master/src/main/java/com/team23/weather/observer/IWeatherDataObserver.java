package com.team23.weather.observer;

import com.team23.weather.models.WeatherData;

/**
 * Using this interface, an object can handle changes in the {@link com.team23.weather.models.WeatherData} model.
 * Changes to the {@link com.team23.weather.models.WeatherData} model are appropriated by {@link com.team23.weather.App}.
 *
 * @author Saquib Mian
 */
public interface IWeatherDataObserver {
    /**
     * Before the {@link com.team23.weather.models.WeatherData} object changes
     * @param weatherData the previous representation of the {@link com.team23.weather.models.WeatherData} object
     */
    void beforeWeatherDataUpdate(WeatherData weatherData);

    /**
     * After the {@link com.team23.weather.models.WeatherData} object has changed
     * @param weatherData the new representation of the {@link com.team23.weather.models.WeatherData} object
     */
    void onWeatherDataUpdated(WeatherData weatherData);
}
