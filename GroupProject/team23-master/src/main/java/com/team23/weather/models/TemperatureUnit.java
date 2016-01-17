package com.team23.weather.models;

/**
 * The different {@link com.team23.weather.models.Temperature} units
 *
 * @author Saquib Mian
 */
public enum TemperatureUnit {
	Celsius,
    Farenheit, 
    Kelvin;

    /**
     * @return the string representation of this unit
     */
    public String toString() {
        switch (this) {
            case Celsius:
                return "°C";
            case Farenheit:
                return "°F";
            case Kelvin:
                return "K";
            default:
                throw new RuntimeException("You done goofd");
        }
    }
}
