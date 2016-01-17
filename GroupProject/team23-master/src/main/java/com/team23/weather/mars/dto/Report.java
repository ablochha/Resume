package com.team23.weather.mars.dto;

/**
 * A Report represents Mars' most recent weather forecast. This object is
 * filled when the JSON string is decoded. A weather forecast consists of
 * the date and season on both Earth and Mars, the minimum and maximum
 * temperatures over the day, the pressure, the humidity, the wind speed
 * and direction, the atmosphere opacity, the sunrise, and the sunset.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.mars.IMarsWeatherClient
 * @see com.team23.weather.mars.MarsWeatherClient
 * @see com.team23.weather.mars.dto.MarsWeatherResponse
 * @see com.team23.weather.models.MarsForecast
 * @see com.team23.weather.serialization.ISerializer
 * 
 */
public class Report {

	/**
	 * terrestrial_date contains the date on earth corresponding to the time of the mars forecast.
	 */
	private String terrestrial_date;
	
	/**
	 * sol contains the date on mars for the forecast.
	 */
    private int sol;
    
    /**
     * ls contains the season on mars.
     */
    private double ls;
    
    /**
     * min_temp contains the minimum temperature over the forecast period.
     */
    private double min_temp;
    
    /**
     * min_temp_fahrenheit contains the minimum temperature in Fahrenheit.
     */
    private double min_temp_fahrenheit;
    
    /**
     * max_temp contains the the maximum temperature over the forecast period.
     */
    private double max_temp;
    
    /**
     * max_temp_fahrenheit contains the maximum temperature in Fahrenheit.
     */
    private double max_temp_fahrenheit;
    
    /**
     * pressure contains the atmospheric pressure on Mars.
     */
    private double pressure;
    
    /**
     * pressure_string contains a written description of the pressure.
     */
    private String pressure_string;
    
    /**
     * abs_humidty contains the absolute humidity on Mars.
     */
    private int abs_humidity;
    
    /**
     * wind_speed contains the speed of the wind on Mars.
     */
    private double wind_speed;
    
    /**
     * wind_direction contains the direction of the wind on Mars.
     */
    private String wind_direction;
    
    /**
     * atmo_opacity contains a written description of the sky condition.
     */
    private String atmo_opacity;
    
    /**
     * season contains the month on Earth corresponding to this forecast.
     */
    private String season;
    
    /**
     * sunrise contains the time when the sun rose at that date.
     */    
    private String sunrise;
    
    /**
     * sunset contains the time when the sun set at that date.
     */
    private String sunset;	
    
    /**
     * Returns the date on Earth corresponding to the Mars weather forecast.
     * @return the date on Earth corresponding to the Mars weather forecast
     */
    public String getTerrestrialDate() {
    	
    	return terrestrial_date;
    	
    } //end getTerrestrialDate
    
    /**
     * Sets the Earth's date for the weather forecast on Mars.
     * @param terrestrial_date the date on Earth corresponding to the Mars weather forecast
     */
    public void setTerrestrialDate(String terrestrial_date) {
    	
    	this.terrestrial_date = terrestrial_date;
    	
    } //end setTerrestrialDate
    
    /**
     * Returns the date of the weather forecast on Mars.
     * @return the date of the weather forecast on Mars
     */
    public int getSol() {
    	
    	return sol;
    	
    } //end getSol
    
    /**
     * Sets the date of the weather forecast on Mars.
     * @param sol the date of the weather forecast on Mars
     */
    public void setSol(int sol) {
    	
    	this.sol = sol;
    	
    } //end setSol
    
    /**
     * Returns the season on Mars.
     * @return the season on Mars
     */
    public Double getLS() {
    	
    	return ls;
    	
    } //end getLS
    
    /** 
     * Sets the season on Mars.
     * @param ls the season on Mars
     */
    public void setLS(Double ls) {
    	
    	this.ls = ls;
    	
    } //end setLS
    
    /**
     * Returns the minimum temperature for this forecast.
     * @return the minimum temperature for this forecast
     */
    public Double getMinTemp() {
    	
    	return min_temp;
    	
    } //end getMinTemp
    
    /**
     * Sets the minimum temperature for this forecast.
     * @param min_temp the minimum temperature for this forecast
     */
    public void setMinTemp(Double min_temp) {
    	
    	this.min_temp = min_temp;
    	
    } //end setMinTemp
    
    /**
     * Returns the minimum temperature for this forecast in fahrenheit.
     * @return the minimum temperature for this forecast in fahrenheit
     */
    public Double getMinTempFahrenheit() {
    	
    	return min_temp_fahrenheit;
    	
    } //end getMinTempFahrenheit
    
    /**
     * Sets the minimum temperature for this forecast in fahrenheit.
     * @param min_temp_fahrenheit the minimum temperature for this forecast in fahrenheit
     */
    public void setMinTempFahrenheit(Double min_temp_fahrenheit) {
    	
    	this.min_temp_fahrenheit = min_temp_fahrenheit;
    	
    } //end setMinTempFahrenheit
    
    /**
     * Returns the maximum temperature for this forecast.
     * @return the maximum temperature for this forecast
     */
    public Double getMaxTemp() {
    	
    	return max_temp;
    	
    } //end getMaxTemp
    
    /**
     * Sets the maximum temperature for this forecast.
     * @param max_temp the maximum temperature for this forecast
     */
    public void setMaxTemp(Double max_temp) {
    	
    	this.max_temp = max_temp;
    	
    } //end setMaxTemp
    
    /**
     * Returns the maximum temperature for this forecast in fahrenheit.
     * @return the maximum temperature for this forecast in fahreneheit
     */
    public Double getMaxTempFahrenheit() {
    	
    	return max_temp_fahrenheit;
    	
    } //end getMaxTempFahrenheit
    
    /**
     * Sets the maximum temperature for this forecast in fahrenheit.
     * @param max_temp_fahrenheit the maximum temperature for this forecast in fahreneheit
     */
    public void setMaxTempFahrenheit(Double max_temp_fahrenheit) {
    	
    	this.max_temp_fahrenheit = max_temp_fahrenheit;
    	
    } //end setMaxTempFahrenheit
    
    /**
     * Returns the atmospheric pressure for this forecast.
     * @return the atmospheric pressure for this forecast.
     */
    public Double getPressure() {
    	
    	return pressure;
    	
    } //end getPressure
    
    /**
     * Sets the atmospheric pressure for this forecast.
     * @param pressure the atmospheric pressure for this forecast
     */
    public void setPressure(Double pressure) {
    	
    	this.pressure = pressure;
    	
    } //end setPressure
    
    /**
     * Returns a description of the atmospheric pressure for this forecast.
     * @return the a description of the atmospheric pressure for this forecast
     */
    public String getPressureString() {
    	
    	return pressure_string;
    	
    } //end getPressureString
    
    /**
     * Sets the description of the atmospheric pressure for this forecast.
     * @param pressure_string a description of the atmospheric pressure for this forecast
     */
    public void setPressureString(String pressure_string) {
    	
    	this.pressure_string = pressure_string;
    	
    } //end setPressureString
    
    /**
     * Returns the absolute humidity for this forecast.
     * @return the absolute humidity for this forecast
     */
    public int getAbsHumidity() {
    	
    	return abs_humidity;
    	
    } //end getAbsHumidity
    
    /**
     * Sets the absolute humidity for this forecast.
     * @param abs_humidity the absolute humidity for this forecast
     */
    public void setAbsHumidity(int abs_humidity) {
    	
    	this.abs_humidity = abs_humidity;
    	
    } //end setAbsHumidity
    
    /**
     * Returns the wind speed for this forecast.
     * @return the wind speed for this forecast
     */
    public Double getWindSpeed() {
    	
    	return wind_speed;
    	
    } //end getWindSpeed
    
    /**
     * Sets the wind speed for this forecast.
     * @param wind_speed the wind speed for this forecast
     */
    public void setWindSpeed(Double wind_speed) {
    	
    	this.wind_speed = wind_speed;
    	
    } //end setWindSpeed
    
    /**
     * Returns the wind direction for this forecast.
     * @return the wind direction for this forecast
     */
    public String getWindDirection() {
    	
    	return wind_direction;
    	
    } //end getWindDirection
    
    /**
     * Sets the wind direction for this forecast.
     * @param wind_direction the wind direction for this forecast
     */
    public void setWindDirection(String wind_direction) {
    	
    	this.wind_direction = wind_direction;
    	
    } //end setWindDirection
    
    /**
     * Returns a description of the sky condition for this forecast.
     * @return the description of the sky condition for this forecast
     */
    public String getAtmoOpacity() {
    	
    	return atmo_opacity;
    	
    } //end getAtmosphereOpacity
    
    /**
     * Sets the description of the sky condition for this forecast.
     * @param atmo_opacity a description of the sky condition for this forecast
     */
    public void setAtmoOpacity(String atmo_opacity) {
    	
    	this.atmo_opacity = atmo_opacity;
    	
    } //end setAtmosphereOpacity
    
    /**
     * Returns the month on Earth for this forecast.
     * @return the month on Earth for this forecast 
     */
    public String getSeason() {
    	
    	return season;
    	
    } //end getSeason
    
    /**
     * Sets the month on Earth for this forecast.
     * @param season the month on Earth for this forecast
     */
    public void setSeason(String season) {
    	
    	this.season = season;
    	
    } //end setSeason
    
    /**
     * Returns the time the sun rose for this forecast.
     * @return the time the sun rose for this forecast
     */
    public String getSunrise() {
    	
    	return sunrise;
    	
    } //end getSunrise
    
    /**
     * Sets the time the sun rose for this forecast.
     * @param sunrise the time the sun rose for this forecast
     */
    public void setSunrise(String sunrise) {
    	
    	this.sunrise = sunrise;
    	
    } //end setSunrise
    
    /**
     * Returns the time the sun set for this forecast.
     * @return the time the sun set for this forecast
     */
    public String getSunset() {
    	
    	return sunset;
    	
    } //end getSunset
    
    /**
     * Sets the time the sun set for this forecast.
     * @param sunset the time the sun set for this forecast
     */
    public void setSunset(String sunset) {
    	
    	this.sunset = sunset;
    	
    } //end setSunset
	
    /**
     * Returns a String representation of the Report object.
     * @return a string containing values of all the fields in the Report object
     */
    @Override
    public String toString() {
    	
        return "report: {" +
                "terrestrial_date=" + terrestrial_date +
                ", sol=" + sol +
                ", ls=" + ls +
                ", min_temp='" + min_temp + 
                ", min_temp_fahrenheit=" + min_temp_fahrenheit +
                ", max_temp=" + max_temp +
                ", max_temp_fahrenheit=" + max_temp_fahrenheit +
                ", pressure=" + pressure +
                ", pressure_string=" + pressure_string +
                ", abs_humidity=" + abs_humidity + 
                ", wind_speed=" + wind_speed +
                ", wind_direction=" + wind_direction +
                ", atmo_opacitiy=" + atmo_opacity +
                ", season=" + season +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
        
    } //end toString
    
} //end Report