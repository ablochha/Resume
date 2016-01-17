package com.team23.weather.mars;

import com.team23.weather.http.HttpClient;
import com.team23.weather.models.MarsForecast;
import com.team23.weather.mars.dto.*;
import com.team23.weather.serialization.ISerializer;

import java.io.*;

/**
 * A MarsWeatherClient that implements the {@link com.team23.weather.mars.IMarsWeatherClient} interface.
 * 
 * This class connects to the website "http://marsweather.ingenology.com/" in order to view 
 * archived weather information collected by NASA's Curiosity rover. The weather information
 * is stored in JSON format, so this class calls {@link com.team23.weather.serialization.JsonSerializer} to format the weather
 * into a {@link com.team23.weather.models.MarsForecast} object. Finally, this class returns the {@link com.team23.weather.models.MarsForecast}
 * to be displayed to the user.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.mars.IMarsWeatherClient
 * @see com.team23.weather.models.MarsForecast
 * @see com.team23.weather.serialization.ISerializer
 * @see com.team23.weather.http.HttpClient
 * @see com.team23.weather.mars.dto.MarsWeatherResponse
 * @see com.team23.weather.mars.dto.Report
 *
 */
public class MarsWeatherClient implements IMarsWeatherClient {
		
	/**
	 * _baseurl represents the web page that we are trying to connect to.
	 */
	private String _baseUrl;    
	
	/**
	 * _httpClient is capable of reading a stream of information from a website.
	 */
    private final HttpClient _httpClient;
    
    /**
     * _serializer is capable of decoding JSON string format.
     */
    private final ISerializer _serializer;

    /**
     * Constructs an object capable of connecting to the Mars Weather website and translating
     * the weather format into a {@link com.team23.weather.models.MarsForecast} object.
     * @param httpClient the input stream reader to read a website
     * @param serializer the decoder for the JSON string format
     */
    public MarsWeatherClient(HttpClient httpClient, ISerializer serializer) {
    	
        _httpClient = httpClient;
        _serializer = serializer;
        _baseUrl = "http://marsweather.ingenology.com/v1/latest/?format=json";
        
    } //end MarsWeatherClient
        
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseUrl() {
    	
        return _baseUrl;
        
    } //end getBaseUrl

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBaseUrl(String baseUrl) {
    	
        _baseUrl = baseUrl;
        
    } //end setBaseUrl
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MarsForecast getWeather() {
        
        String jsonResponse;

        try {
        	
            jsonResponse = _httpClient.getAsString(_baseUrl);
            
        } //end try 
        
        catch (IOException e) {
        	
            e.printStackTrace();
            // Log no data back
            return null;
            
        } //end catch

        MarsWeatherResponse response = _serializer.deserialize(jsonResponse, MarsWeatherResponse.class);

        return response.getForecast();
        
    } //end getWeather

} //end MarsWeatherClient