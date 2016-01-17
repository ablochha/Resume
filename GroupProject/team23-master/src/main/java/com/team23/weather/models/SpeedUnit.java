package com.team23.weather.models;

/**
* The different {@link com.team23.weather.models.Speed} units
 *
 * @author Saquib Mian
*/
public enum SpeedUnit {
    MPH,
    KMPH;

    /**
     * @return the string representation of this unit
     */
    public String toString() {
        switch(this) {
            case MPH:
                return "mph";
            case KMPH:
                return "km/h";
            default:
                throw new RuntimeException("You done goofd");
        }
    }
}
