package com.team23.weather.mars;

import com.team23.weather.models.MarsForecast;

/**
 * An IMarsWeatherClient is an object that contains the url of NASA's
 * archive of Mars Weather information. This client can read the weather
 * and return it back to another function in a readable format.
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * @see com.team23.weather.mars.MarsWeatherClient
 * @see com.team23.weather.mars.dto.MarsWeatherResponse
 * @see com.team23.weather.mars.dto.Report
 * @see com.team23.weather.models.MarsForecast
 * @see com.team23.weather.serialization.ISerializer
 * @see com.team23.weather.http.HttpClient
 * 
 */
public interface IMarsWeatherClient {

		/**
		 * Returns the url that the {@link com.team23.weather.http.HttpClient} is connected to.
		 * @return the url that the {@link com.team23.weather.http.HttpClient} is connected to
		 */
        public String getBaseUrl();
        
        /**
         * Sets the {@link com.team23.weather.http.HttpClient}'s url to the specified string.
         * @param baseUrl the url of the website to connect to
         */
        public void setBaseUrl(String baseUrl);
 
        /**
         * Returns the weather in a {@link com.team23.weather.models.MarsForecast} object.
         * @return a {@link com.team23.weather.models.MarsForecast} object containing all the mars weather data
         */
        MarsForecast getWeather();
   
} //end IMarsWeatherClient