package com.team23.weather.locale;

import com.team23.weather.models.PressureUnit;
import com.team23.weather.models.SpeedUnit;
import com.team23.weather.models.TemperatureUnit;

/**
 * This class represents units associated with a particular {@link com.team23.weather.locale.LocaleSettings}
 *
 * @author Saquib Mian
 */
public class LocaleUnits {
    /**
     * the temperature unit
     */
    private TemperatureUnit _temperatureUnit;

    /**
     * the speed unit
     */
    private SpeedUnit _speedUnit;

    /**
     * the pressure unit
     */
    private PressureUnit _pressureUnit;

    /**
     * get the temperature unit
     * @return the temperature unit
     */
    public TemperatureUnit getTemperatureUnit() {
        return _temperatureUnit;
    }

    /**
     * set the temperature unit
     * @param temperatureUnit the temperature unit
     */
    public void setTemperatureUnit(TemperatureUnit temperatureUnit) {
        _temperatureUnit = temperatureUnit;
    }

    /**
     * get the speed unit
     * @return the speed unit
     */
    public SpeedUnit getSpeedUnit() {
        return _speedUnit;
    }

    /**
     * get the speed unit
     * @param speedUnit the speed unit
     */
    public void setSpeedUnit(SpeedUnit speedUnit) {
        _speedUnit = speedUnit;
    }

    /**
     * get the pressure unit
     * @return the pressure unit
     */
    public PressureUnit getPressureUnit() {
        return _pressureUnit;
    }

    /**
     * set the pressure unit
     * @param pressureUnit the pressure unit
     */
    public void setPressureUnit(PressureUnit pressureUnit) {
        _pressureUnit = pressureUnit;
    }
}
