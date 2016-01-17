package com.team23.weather.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A MarsForecast represents all the weather information on Mars as described
 * in the project specifications. This includes the date of the forecast (Earth and Mars), 
 * the minimum and maximum {@link com.team23.weather.models.Temperature}'s, the 
 * {@link com.team23.weather.models.Pressure}, the humidity, the wind {@link com.team23.weather.models.Speed}, 
 * the wind direction, and the sky condition. This forecast is then displayed to the user on
 * the {@link com.team23.weather.gui.MarsForecastPanel}.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.mars.MarsWeatherClient
 * @see com.team23.weather.mars.dto.MarsWeatherResponse
 * @see com.team23.weather.mars.dto.Report
 * @see com.team23.weather.gui.MarsForecastPanel
 *
 */
public class MarsForecast {

	/**
	 * _earth_date contains the date on earth corresponding to the time of the mars forecast.
	 */
	private Date _earth_date;
	
	/**
	 * _mars_date contains the date on mars for the forecast.	
	 */
    private int _mars_date;
    
    /**
     * _min_temp contains the minimum temperature over the forecast period.	 
     */
    private Temperature _min_temp;
    
    /**
     * _max_temp contains the the maximum temperature over the forecast period.	 
     */
    private Temperature _max_temp;
    
    /**
     * _pressure contains the atmospheric pressure on Mars.	 
     */
    private Pressure _pressure;
    
    /**
     * _abs_humidty contains the absolute humidity on Mars.
     */
    private int _abs_humidity;
    
    /**
     * _wind_speed contains the speed of the wind on Mars.
     */
    private Speed _wind_speed;
    
    /**
     * _sky_condition contains a written description of the sky condition.
     */
    private String _sky_condition;

    /**
     * URL to image indicating sky condition; this is always sunny.
     */
    private final String _skyConditionIconUrl = "http://openweathermap.org/img/w/01d.png";

    /**
     * Returns the date on Earth corresponding to the Mars weather forecast.
     * @return the date on Earth corresponding to the Mars weather forecast
     */
    public Date getEarthDate() {
    	
    	return _earth_date;
    	
    } //end getEarthDate
    
    /**
     * Sets the Earth's date for the weather forecast on Mars.
     * @param earth_date the date on Earth corresponding to the Mars weather forecast
     */
    public void setEarthDate(String earth_date) {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	try {
    		
    		_earth_date = formatter.parse(earth_date);
    		
    	} //end try
    	
    	catch (ParseException pe) {
    		
    		pe.printStackTrace();
    		
    	} //end catch
    	
    } //end setEarthDate
    
    /**
     * Returns the date of the weather forecast on Mars.
     * @return the date of the weather forecast on Mars
     */
    public int getMarsDate() {
    	
    	return _mars_date;
    	
    } //end getMarsDate
    
    /**
     * Sets the date of the weather forecast on Mars.
     * @param mars_date the date of the weather forecast on Mars
     */
    public void setMarsDate(int mars_date) {
    	
    	_mars_date = mars_date;
    	
    } //end setMarsDate
    
    /**
     * Returns the minimum {@link com.team23.weather.models.Temperature} for this forecast.
     * @return the minimum {@link com.team23.weather.models.Temperature} for this forecast
     */
    public Temperature getMinTemp() {
    	
    	return _min_temp;
    	
    } //end getMinTemp
    
    /**
     * Sets the minimum {@link com.team23.weather.models.Temperature} for this forecast.
     * @param min_temp the minimum {@link com.team23.weather.models.Temperature} for this forecast
     */
    public void setMinTemp(Temperature min_temp) {
    	
    	_min_temp = min_temp;
    	
    } //end setMinTemp
    
    /**
     * Returns the maximum {@link com.team23.weather.models.Temperature} for this forecast.
     * @return the maximum {@link com.team23.weather.models.Temperature} for this forecast
     */
    public Temperature getMaxTemp() {
    	
    	return _max_temp;
    	
    } //end getMaxTemp
    
    /**
     * Sets the maximum {@link com.team23.weather.models.Temperature} for this forecast.
     * @param max_temp the maximum {@link com.team23.weather.models.Temperature} for this forecast
     */
    public void setMaxTemp(Temperature max_temp) {
    	
    	_max_temp = max_temp;
    	
    } //end setMaxTemp
    
    /**
     * Returns the atmospheric {@link com.team23.weather.models.Pressure} for this forecast.
     * @return the atmospheric {@link com.team23.weather.models.Pressure} for this forecast.
     */
    public Pressure getPressure() {
    	
    	return _pressure;
    	
    } //end getPressure
    
    /**
     * Sets the atmospheric {@link com.team23.weather.models.Pressure} for this forecast.
     * @param pressure the atmospheric {@link com.team23.weather.models.Pressure} for this forecast
     */
    public void setPressure(Pressure pressure) {
    	
    	_pressure = pressure;
    	
    } //end setPressure
    
    /**
     * Returns the absolute humidity for this forecast.
     * @return the absolute humidity for this forecast
     */
    public int getAbsHumidity() {	
    		
    	return _abs_humidity;  	  		
    		
    } //end getAbsHumidity
    
    /**
     * Sets the absolute humidity for this forecast.
     * @param abs_humidity the absolute humidity for this forecast
     */
    public void setAbsHumidity(int abs_humidity) {
    	
    	_abs_humidity = abs_humidity;
    	
    } //end setAbsHumidity
    
    /**
     * Returns the wind {@link com.team23.weather.models.Speed} for this forecast.
     * @return the wind {@link com.team23.weather.models.Speed} for this forecast
     */
    public Speed getWindSpeed() {
    	
    	return _wind_speed;
    	
    } //end getWindSpeed
    
    /**
     * Sets the wind {@link com.team23.weather.models.Speed} for this forecast.
     * @param wind_speed the wind {@link com.team23.weather.models.Speed} for this forecast
     */
    public void setWindSpeed(Speed wind_speed) {
    	
    	_wind_speed = wind_speed;
    	
    } //end setWindSpeed

    /**
     * Returns a description of the sky condition for this forecast.
     * @return the description of the sky condition for this forecast
     */
    public String getSkyCondition() {
    	
    	return _sky_condition;
    	
    } //end getSkyCondition

    public String getSkyConditionIconUrl() {
        return _skyConditionIconUrl;
    }
    
    /**
     * Sets the description of the sky condition for this forecast.
     * @param sky_condition a description of the sky condition for this forecast
     */
    public void setSkyCondition(String sky_condition) {
    	
    	_sky_condition = sky_condition;
    	
    } //end setSkyCondition
    
    /**
     * Returns a String representation of the {@link com.team23.weather.models.MarsForecast} object.
     * @return a string containing values of all the fields in the {@link com.team23.weather.models.MarsForecast} object
     */
    @Override
    public String toString() {
    	
        return String.format(
        		
                "_earth_date: %s, _mars_date: %d, _min_temp: %s, _max_temp: %s, _pressure: %s, _abs_humidity: %d, _wind_speed:, %s, _sky_condition: %s",
                _earth_date.toString(), _mars_date, _min_temp.toString(), _max_temp.toString(), _pressure.toString(), _abs_humidity, _wind_speed, _sky_condition
                
        ); //end format
        
    } //end toString
    
} //end MarsForecast