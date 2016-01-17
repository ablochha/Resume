package com.team23.weather.models;

/**
 * Wraps {@link com.team23.weather.models.Forecast} to provide a
 * model that contains multiple {@link com.team23.weather.models.Forecast}
 * objects. This is used to represent the hourly and daily forecasts.
 *
 * @author Saquib Mian
 */
public class CompoundForecast {
    /**
     * is this forecast the hourly forecast?
     */
    private final boolean _isHourly;

    /**
     * is this forecast the weekly forecast?
     */
    private final boolean _isWeekly;

    /**
     * the list of individual {@link com.team23.weather.models.Forecast} objects contained in this {@link com.team23.weather.models.CompoundForecast}
     */
    private final Forecast[] _forecasts;

    /**
     *
     * @param forecasts the forecasts
     * @param isHourly is this forecast the hourly forecast?
     * @param isWeekly is this forecast the weekly forecast?
     */
    public CompoundForecast(Forecast[] forecasts, boolean isHourly, boolean isWeekly) {
        _isHourly = isHourly;
        _isWeekly = isWeekly;
        _forecasts = forecasts;
    }

    /**
     *
     * @return true if this forecast is a hourly forecast, false otherwise
     */
    public boolean isHourly() {
        return _isHourly;
    }

    /**
     *
     * @return true if this forecast is a weekly forecast, false otherwise
     */
    public boolean isWeekly() {
        return _isWeekly;
    }

    /**
     *
     * @return the {@link com.team23.weather.models.Forecast} objects in this {@link com.team23.weather.models.CompoundForecast}
     */
    public Forecast[] getForecasts() {
        return _forecasts;
    }
}
