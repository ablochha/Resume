package com.team23.weather.models;

import com.team23.weather.locale.LocaleSettings;

/**
 * This model represents the temperature measure, with its unit
 *
 * @author Saquib Mian
 */
public class Temperature {

    /**
     * the value
     */
    private final double _value;

    /**
     * the unit
     */
    private final TemperatureUnit _unit;

    /**
     * @param value the value
     * @param unit the {@link com.team23.weather.models.TemperatureUnit}
     */
    public Temperature(double value, TemperatureUnit unit) {
        _value = value;
        _unit = unit;
    }

    /**
     * Calculates the value of this {@link com.team23.weather.models.Temperature} into the specified unit
     * @param unit the {@link com.team23.weather.models.TemperatureUnit} to calculate
     * @return a calculated value
     */
    public double getValueForUnit(TemperatureUnit unit) {
        if (_unit == unit) {
            return _value;
        }

        switch (unit) {
            case Kelvin:
                return convertToKelvin();
            case Celsius:
                return convertToCelsius();
            case Farenheit:
                return convertToFarenheit();
        }

        return Double.MIN_VALUE;
    }

    /**
     * Convert to Kelvin
     * @return the converted value
     */
    private double convertToKelvin() {
        switch (_unit) {
            case Kelvin:
                return _value;
            case Celsius:
                return _value + 273.15;
            case Farenheit:
                return (_value - 32) / 1.8 + 273.15;
        }

        return Double.MIN_VALUE;
    }

    /**
     * Convert to Celsius
     * @return the converted value
     */
    private double convertToCelsius() {
        switch (_unit) {
            case Kelvin:
                return _value - 273.15;
            case Celsius:
                return _value;
            case Farenheit:
                return (_value - 32) / 1.8;
        }

        return Double.MIN_VALUE;
    }

    /**
     * Convert to Fahrenheit
     * @return the converted value
     */
    private double convertToFarenheit() {
        switch (_unit) {
            case Kelvin:
                return ((_value - 273.15) * 1.8) + 32;
            case Celsius:
                return (_value * 1.8) + 32;
            case Farenheit:
                return _value;
        }

        return Double.MIN_VALUE;
    }

    @Override
    public String toString() {
        return toStringImpl(_unit);
    }

    /**
     * Returns the string representation of this value using the given {@link com.team23.weather.locale.LocaleSettings}
     * @param localeSettings the locale settings to use for the string representation
     * @return the string representation
     */
    public String toString(LocaleSettings localeSettings) {
        TemperatureUnit unit = localeSettings.getUnits().getTemperatureUnit();
        return toStringImpl(unit);
    }

    /**
     * Returns the string representation of this value using the given {@link com.team23.weather.models.TemperatureUnit}
     * @param unit the unit to use for the string representation
     * @return the string representation
     */
    private String toStringImpl(TemperatureUnit unit) {
        return String.format("%.0f%s", getValueForUnit(unit), unit);
    }
}
