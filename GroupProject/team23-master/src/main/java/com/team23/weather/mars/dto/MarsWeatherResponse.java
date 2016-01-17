package com.team23.weather.mars.dto;

import com.team23.weather.models.*;

/**
 * A MarsWeatherResponse is an object that copies the format of the
 * JSON string representing Mars' weather. Mars' weather archive consists of
 * both a "latest" weather forecast and an extensive "archive" of forecasts.
 * MarsWeatherResponse is set up to only read the "latest" weather forecast. *
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.mars.IMarsWeatherClient
 * @see com.team23.weather.mars.MarsWeatherClient
 * @see com.team23.weather.models.MarsForecast
 * @see com.team23.weather.serialization.ISerializer
 * @see com.team23.weather.mars.dto.Report
 *
 */
public class MarsWeatherResponse {
	
	/**
	 * report contains all the weather information from a "latest" JSON string.
	 */	
	private Report report;
	
	
	/**
     * Returns the {@link com.team23.weather.mars.dto.Report} from the "latest" Mars forecast.
     * @return the {@link com.team23.weather.mars.dto.Report} from the "latest" Mars forecast
     */
	public Report getReport() {
		
		return report;
		
	} //end getReport
	
	/**
	 * Fills the {@link com.team23.weather.mars.dto.Report} with forecast information from JSONSerializer.
	 * @param report the {@link com.team23.weather.mars.dto.Report} containing Mars' weather forecast
	 */	
	public void setReport(Report report) {
		
		this.report = report;
		
	} //end setReport
	    
	/**
	 * Returns a {@link com.team23.weather.models.MarsForecast} as specified in our project specifications.
	 * @return a {@link com.team23.weather.models.MarsForecast}
	 */
    public MarsForecast getForecast() {
    	
        MarsForecast toReturn = new MarsForecast();

        //Set all the fields specified in the project specifications
        toReturn.setEarthDate(report.getTerrestrialDate());
        toReturn.setMarsDate(report.getSol());
        toReturn.setMinTemp(new Temperature(report.getMinTemp(), TemperatureUnit.Celsius));
        toReturn.setMaxTemp(new Temperature(report.getMaxTemp(), TemperatureUnit.Celsius));
        toReturn.setPressure(new Pressure(report.getPressure(), PressureUnit.Pascal));
        toReturn.setAbsHumidity(report.getAbsHumidity());        
        toReturn.setWindSpeed(new Speed(report.getWindSpeed(), SpeedUnit.KMPH));
        toReturn.setSkyCondition(report.getAtmoOpacity());

        return toReturn;
        
    } //end getForecast       
    
} //end MarsWeatherResponse