package com.team23.weather.models;

/**
 * The different {@link com.team23.weather.models.Pressure} units
 *
 * @author Saquib Mian
 */
public enum PressureUnit {
    Pascal,
    Mercury;

    /**
     * @return the string representation of this unit
     */
    public String toString() {
        switch(this) {
            case Pascal:
                return "Pa";
            case Mercury:
                return "Hg";
            default:
                throw new RuntimeException("You done goofd");
        }
    }
}
