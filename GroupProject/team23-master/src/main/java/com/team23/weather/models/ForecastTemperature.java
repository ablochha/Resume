package com.team23.weather.models;

/**
 * This model holds multiple {@link com.team23.weather.models.Temperature}s to represent a high/current/low structure.
 *
 * @author Saquib Mian
 */
public class ForecastTemperature {

    /**
     * the high temperature
     */
    private Temperature _high;

    /**
     * the current temperature
     */
    private Temperature _current;

    /**
     * the low temperature
     */
    private Temperature _low;

    /**
     *
     * @param high the high temperature
     * @param current the current temperature
     * @param low the low temperature
     */
    public ForecastTemperature(Temperature high, Temperature current, Temperature low) {
        _high = high;
        _current = current;
        _low = low;
    }

    /**
     * get the high temperature
     * @return the high temperature
     */
    public Temperature getHigh() {
        return _high;
    }

    /**
     * set the high temperature
     * @param high the high temperature
     */
    public void setHigh(Temperature high) {
        _high = high;
    }

    /**
     * get the current temperature
     * @return the current temperature
     */
    public Temperature getCurrent() {
        return _current;
    }

    /**
     * set the current temperature
     * @param current the current temperature
     */
    public void setCurrent(Temperature current) {
        _current = current;
    }

    /**
     * get the low temperature
     * @return the low temperature
     */
    public Temperature getLow() {
        return _low;
    }

    /**
     * get the low temperature
     * @param low the low temperature
     */
    public void setLow(Temperature low) {
        _low = low;
    }
}
