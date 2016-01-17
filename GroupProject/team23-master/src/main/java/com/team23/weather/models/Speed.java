package com.team23.weather.models;

import com.team23.weather.locale.LocaleSettings;

/**
 * This model represents the speed measure, with its unit
 *
 * @author Saquib Mian
 */
public class Speed {

    /**
     * the value
     */
    private final double _value;

    /**
     * the unit
     */
    private final SpeedUnit _unit;


    /**
     * @param value the value
     * @param unit the {@link com.team23.weather.models.SpeedUnit}
     */
    public Speed(double value, SpeedUnit unit) {
        _value = value;
        _unit = unit;
    }

    /**
     * Calculates the value of this {@link com.team23.weather.models.Speed} into the specified unit
     * @param unit the {@link com.team23.weather.models.SpeedUnit} to calculate
     * @return a calculated value
     */
    public double getValueForUnit(SpeedUnit unit) {
        if (_unit == unit) {
            return _value;
        }

        switch (unit) {
            case MPH:
                return convertToMPH();
            case KMPH:
                return convertToKMPH();
        }

        return Double.MIN_VALUE;
    }

    /**
     * Convert from MPH to KMPH
     * @return the converted value
     */
    private double convertToKMPH() {
        return _value / 1.60934;
    }

    /**
     * Convert from KMPH to MPH
     * @return the converted value
     */
    private double convertToMPH() {
        return _value * 1.60934;
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
        SpeedUnit unit = localeSettings.getUnits().getSpeedUnit();
        return toStringImpl(unit);
    }

    /**
     * Returns the string representation of this value using the given {@link com.team23.weather.models.SpeedUnit}
     * @param unit the unit to use for the string representation
     * @return the string representation
     */
    private String toStringImpl(SpeedUnit unit) {
        return String.format("%.0f %s", getValueForUnit(unit), unit);
    }
}
