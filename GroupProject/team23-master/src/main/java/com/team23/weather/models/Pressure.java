package com.team23.weather.models;

import com.team23.weather.locale.LocaleSettings;

/**
 * This model represents a pressure value, with its unit.
 *
 * @author Saquib Mian
 */
public class Pressure {

    /**
     * the value
     */
    private final double _value;

    /**
     * the unit
     */
    private final PressureUnit _unit;

    /**
     * @param value the value
     * @param unit the {@link com.team23.weather.models.PressureUnit}
     */
    public Pressure(double value, PressureUnit unit) {
        _value = value;
        _unit = unit;
    }

    /**
     * Calculates the value of this {@link com.team23.weather.models.Pressure} into the specified unit
     * @param unit the {@link com.team23.weather.models.PressureUnit} to calculate
     * @return a calculated value
     */
    public double getValueForUnit(PressureUnit unit) {
        if (_unit == unit) {
            return _value;
        }

        switch (unit) {
            case Pascal:
                return convertToPascal();
            case Mercury:
                return convertToMercury();
        }

        throw new RuntimeException("You done goofd");
    }

    /**
     * Convert from Pascal to Mercury
     * @return the converted value
     */
    private double convertToMercury() {
        return _value * 0.00750061683;
    }

    /**
     * Convert from Mercury to Pascal
     * @return the converted value
     */
    private double convertToPascal() {
        return _value / 0.00750061683;
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
        PressureUnit unit = localeSettings.getUnits().getPressureUnit();
        return toStringImpl(unit);
    }

    /**
     * Returns the string representation of this value using the given {@link com.team23.weather.locale.LocaleUnits}
     * @param unit the unit to use for the string representation
     * @return the string representation
     */
    private String toStringImpl(PressureUnit unit) {
        if(unit == PressureUnit.Pascal) {
            return String.format("%.1f k%s", getValueForUnit(unit)/10, unit);
        } else if (unit == PressureUnit.Mercury) {
            return String.format("%.1f mm%s", getValueForUnit(unit)*100, unit);
        }

        throw new RuntimeException("You done goofd");
    }
}
