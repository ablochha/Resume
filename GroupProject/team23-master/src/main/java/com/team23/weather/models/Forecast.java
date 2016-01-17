package com.team23.weather.models;

import java.util.Date;

/**
 * The main weather model. Holds all domain-relevant data.
 *
 * @author Saquib Mian
 */
public class Forecast {

    /**
     * the humidity
     */
    private double _humidity;

    /**
     * the location of the forecast
     */
    private Location _locationInformation;

    /**
     * the pressure
     */
    private Pressure _pressure;

    /**
     * the sky condition
     */
    private SkyCondition _skyCondition;

    /**
     * sunrise time
     */
    private Date _sunrise;

    /**
     * sunset time
     */
    private Date _sunset;

    /**
     * the {@link com.team23.weather.models.ForecastTemperature} compound temperature
     */
    private ForecastTemperature _temperature;

    /**
     * the time associated with this forecast
     */
    private Date _generationTime;

    /**
     * the wind velocity
     */
    private Velocity _windVelocity;

    /**
     * get the humidity
     * @return the humidity
     */
    public double getHumidity() {
        return _humidity;
    }

    /**
     * set the humidity
     * @param humidity the humidity
     */
    public void setHumidity(double humidity) {
        _humidity = humidity;
    }

    /**
     * get the location
     * @return the {@link com.team23.weather.models.Location}
     */
    public Location getLocationInformation() {
        return _locationInformation;
    }

    /**
     * set the location
     * @param locationInformation the {@link com.team23.weather.models.Location}
     */
    public void setLocationInformation(Location locationInformation) {
        _locationInformation = locationInformation;
    }

    /**
     * get the pressure
     * @return the {@link com.team23.weather.models.Pressure}
     */
    public Pressure getPressure() {
        return _pressure;
    }

    /**
     * set the pressure
     * @param pressure the {@link com.team23.weather.models.Pressure}
     */
    public void setPressure(Pressure pressure) {
        _pressure = pressure;
    }

    /**
     * get the sky condition
     * @return the {@link com.team23.weather.models.SkyCondition}
     */
    public SkyCondition getSkyCondition() {
        return _skyCondition;
    }

    /**
     * set the sky condition
     * @param skyCondition the {@link com.team23.weather.models.SkyCondition}
     */
    public void setSkyCondition(SkyCondition skyCondition) {
        _skyCondition = skyCondition;
    }

    /**
     * get the sunrise
     * @return the sunrise as a {@link java.util.Date}
     */
    public Date getSunrise() {
        return _sunrise;
    }

    /**
     * set the date
     * @param sunrise the date as a {@link java.util.Date}
     */
    public void setSunrise(Date sunrise) {
        _sunrise = sunrise;
    }

    /**
     * get the sunset
     * @return the sunset as a {@link java.util.Date}
     */
    public Date getSunset() {
        return _sunset;
    }

    /**
     * set the sunset
     * @param sunset the sunset as a {@link java.util.Date}
     */
    public void setSunset(Date sunset) {
        _sunset = sunset;
    }

    /**
     * get the temperature
     * @return the {@link com.team23.weather.models.ForecastTemperature}
     */
    public ForecastTemperature getTemperature() {
        return _temperature;
    }

    /**
     * set the temperature
     * @param temperature the temperature as a {@link com.team23.weather.models.ForecastTemperature}
     */
    public void setTemperature(ForecastTemperature temperature) {
        _temperature = temperature;
    }

    /**
     * get the time associated with this forecast
     * @return the time associated with this forecast
     */
    public Date getGenerationTime() {
        return _generationTime;
    }

    /**
     * set the time associated with this forecast
     * @param generationTime the time associated with this forecast
     */
    public void setGenerationTime(Date generationTime) {
        _generationTime = generationTime;
    }

    /**
     * get the wind velocity
     * @return the wind {@link com.team23.weather.models.Velocity}
     */
    public Velocity getWindVelocity() {
        return _windVelocity;
    }

    /**
     * set the wind velocity
     * @param windVelocity the wind {@link com.team23.weather.models.Velocity}
     */
    public void setWindVelocity(Velocity windVelocity) {
        _windVelocity = windVelocity;
    }
}
