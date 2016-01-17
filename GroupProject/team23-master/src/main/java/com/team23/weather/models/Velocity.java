package com.team23.weather.models;

import com.team23.weather.locale.LocaleSettings;

/**
 * This model holds the velocity measure as a {@link com.team23.weather.models.Speed} and its unit
 *
 * @author Saquib Mian
 */
public class Velocity {
    /**
     * The speed
     */
    private final Speed _speed;

    /**
     * The direction as degrees from North
     */
    private final double _direction;

    /**
     * @param value the speed value
     * @param direction the direction from north
     * @param unit the {@link com.team23.weather.models.SpeedUnit}
     */
    public Velocity(double value, double direction, SpeedUnit unit) {
        _speed = new Speed(value,unit);
        _direction = direction;
    }

    /**
     * get the direction from north
     * @return the direction from north
     */
    public double getDirection() {
        return _direction;
    }

    @Override
    public String toString() {
        return String.format("%s %.0f° from N", _speed, _direction);
    }

    /**
     * Returns the string representation of this value using the given {@link com.team23.weather.locale.LocaleSettings}
     * @param localeSettings the locale settings to use for the string representation
     * @return the string representation
     */
    public String toString(LocaleSettings localeSettings) {
        return String.format("%s %.0f° from N", _speed.toString(localeSettings), _direction);
    }
}
