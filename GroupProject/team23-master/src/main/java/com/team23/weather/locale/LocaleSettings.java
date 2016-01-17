package com.team23.weather.locale;

import com.team23.weather.models.PressureUnit;
import com.team23.weather.models.SpeedUnit;
import com.team23.weather.models.TemperatureUnit;

/**
 * This class represents any locale-specific settings like {@link com.team23.weather.locale.LocaleUnits} to use when formatting units, etc.
 *
 * @author Saquib Mian
 */
public class LocaleSettings {
    /**
     * The name of the settings (e.g., Metric)
     */
    private String _name;

    /**
     * The local units
     */
    private LocaleUnits _units;

    private LocaleSettings(String name) {
        _name = name;
        _units = new LocaleUnits();
    }

    /**
     * @return Metric {@link com.team23.weather.locale.LocaleSettings}
     */
    public static LocaleSettings getMetricSettings() {
        LocaleSettings toReturn = new LocaleSettings("Metric");
        toReturn.getUnits().setPressureUnit(PressureUnit.Pascal);
        toReturn.getUnits().setSpeedUnit(SpeedUnit.KMPH);
        toReturn.getUnits().setTemperatureUnit(TemperatureUnit.Celsius);
        return toReturn;
    }

    /**
     * @return Imperial {@link com.team23.weather.locale.LocaleSettings}
     */
    public static LocaleSettings getImperialSettings() {
        LocaleSettings toReturn = new LocaleSettings("Imperial");
        toReturn.getUnits().setPressureUnit(PressureUnit.Mercury);
        toReturn.getUnits().setSpeedUnit(SpeedUnit.MPH);
        toReturn.getUnits().setTemperatureUnit(TemperatureUnit.Farenheit);
        return toReturn;
    }

    /**
     * get the name of the locale
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * set the name of the locale
     * @param name the new name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * get the {@link com.team23.weather.locale.LocaleUnits}
     * @return the {@link com.team23.weather.locale.LocaleUnits}
     */
    public LocaleUnits getUnits() {
        return _units;
    }

    /**
     * set the {@link com.team23.weather.locale.LocaleUnits}
     * @param units the {@link com.team23.weather.locale.LocaleUnits}
     */
    public void setUnits(LocaleUnits units) {
        _units = units;
    }
}
