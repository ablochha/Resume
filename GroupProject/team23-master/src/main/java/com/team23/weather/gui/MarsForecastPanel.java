package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.formatting.Formatter;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Location;
import com.team23.weather.models.MarsForecast;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.ILocationObserver;

import javax.swing.*;
import java.awt.*;

/**
 * A MarsForecastPanel extends {@link javax.swing.JPanel}.
 * A MarsForecastPanel implements the {@link com.team23.weather.observer.ILocationObserver}
 * interface, to listen for location changes.
 * 
 * A MarsForecastPanel implements the {@link com.team23.weather.observer.ILocaleObserver}
 * interface, to listen to locale changes.
 * 
 * A MarsForecastPanel displays all the weather information from a {@link com.team23.weather.models.MarsForecast}
 * to the user. This includes the date of the forecast (earth and mars), 
 * the minimum and maximum {@link com.team23.weather.models.Temperature}'s, the 
 * {@link com.team23.weather.models.Pressure}, the humidity, the wind {@link com.team23.weather.models.Speed}, 
 * the wind direction, and the sky condition. 
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.mars.MarsWeatherClient
 * @see com.team23.weather.models.MarsForecast
 * @see com.team23.weather.models.WeatherData
 * @see javax.swing.JPanel
 *
 */
public class MarsForecastPanel extends JPanel implements ILocaleObserver, ILocationObserver {

	/**
	 * _app contains a reference to the backend of the program.
	 */
	private final App _app;

    /**
     * lblEarthDateData contains Earth date from the forecast.
     */
    private final LazyImageLabel lblMarsSkyConditionImage;
    /**
     * lblEarthDateData contains Earth date from the forecast.
     */
    private final JLabel lblMarsEarthDateData;

    /**
	 * lblMarsDateData contains Mars date from the forecast.
	 */
    private final JLabel lblMarsDateData;
    
    /**
     * lblMarsMaxMinTemperatureData contains the maximum and minimum temperature from the forecast.
     */
	private final JLabel lblMarsMaxMinTemperatureData;
	
	/**
	 * lblMarsPressureData contains the atmospheric pressure from the forecast.
	 */
    private final JLabel lblMarsPressureData;
    
    /**
     * lblAbsoluteHumidityData contains the humidity from the forecast.
     */
    private final JLabel lblMarsAbsoluteHumidityData;
    
    /**
     * lblMarsWindSpeedData contains the wind speed from the forecast.
     */
    private final JLabel lblMarsWindSpeedData;
    
    /**
     * lblMarsSkyConditionData contains a description of the sky condition from the forecast.
     */
    private final JLabel lblMarsSkyConditionData;
	
    /**
     * Constructs a {@link javax.swing.JPanel} with {@link javax.swing.JLabel}'s
     * to display the information from a {@link com.team23.weather.models.MarsForecast}.
     * @param app a reference to {@link com.team23.weather.App}
     */
	public MarsForecastPanel(App app)  {
		
		_app = app;
        _app.registerObserver((ILocaleObserver) this);
        _app.registerObserver((ILocationObserver) this);

        //Labels for Mars Forecast Information
        JLabel lblMarsWeather = new JLabel("Mars Forecast");
        lblMarsSkyConditionImage = new LazyImageLabel();
        lblMarsEarthDateData = new JLabel();
        lblMarsDateData = new JLabel();
        lblMarsMaxMinTemperatureData = new JLabel();
        lblMarsPressureData = new JLabel();
        lblMarsAbsoluteHumidityData = new JLabel();
        lblMarsWindSpeedData = new JLabel();
        lblMarsSkyConditionData = new JLabel();
		
        lblMarsWeather.setFont(Fonts.Large);
        lblMarsEarthDateData.setFont(Fonts.Medium);
        lblMarsDateData.setFont(Fonts.Medium);
        lblMarsMaxMinTemperatureData.setFont(Fonts.Medium);
        lblMarsPressureData.setFont(Fonts.Medium);
        lblMarsAbsoluteHumidityData.setFont(Fonts.Medium);
        lblMarsWindSpeedData.setFont(Fonts.Medium);
        lblMarsSkyConditionData.setFont(Fonts.Medium);

        JPanel infoPanel = new JPanel(new GridLayout(3, 3));
        infoPanel.add(new JComponentLabel("Earth Date", lblMarsEarthDateData));
        infoPanel.add(new JComponentLabel("Mars Date", lblMarsDateData));
        infoPanel.add(new JComponentLabel("Humidity", lblMarsAbsoluteHumidityData));
        infoPanel.add(new JComponentLabel("Air Pressure", lblMarsPressureData));
        infoPanel.add(new JComponentLabel("Wind Speed", lblMarsWindSpeedData));

        GroupLayout l = new GroupLayout(this);
        l.setAutoCreateGaps(true);
        l.setVerticalGroup(
                l.createSequentialGroup()
                        .addComponent(lblMarsSkyConditionImage)
                        .addComponent(lblMarsWeather)
                        .addGroup(
                                l.createParallelGroup()
                                        .addComponent(lblMarsSkyConditionData)
                                        .addComponent(lblMarsMaxMinTemperatureData)
                        )
                        .addComponent(infoPanel)
        );
        l.setHorizontalGroup(
                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(lblMarsSkyConditionImage)
                        .addComponent(lblMarsWeather)
                        .addGroup(
                                l.createSequentialGroup()
                                        .addComponent(lblMarsSkyConditionData)
                                        .addComponent(lblMarsMaxMinTemperatureData)
                        )
                        .addComponent(infoPanel)
        );

        this.setLayout(l);

        loadWeather();
		
	} //end MarsForecastPanel
	
	/**
	 * Retrieves a {@link com.team23.weather.models.MarsForecast} from {@link com.team23.weather.models.WeatherData}
	 * and updates the display for the user.
	 */
	private void loadWeather() {
		
		new SwingWorker<MarsForecast, Void>() {
		private MarsForecast _forecast;

		/**
		 * Retrieves a {@link com.team23.weather.models.MarsForecast} from {@link com.team23.weather.models.WeatherData}
		 * @return a {@link com.team23.weather.models.MarsForecast} to be displayed to the user
		 * @throws Exception if there was an error retrieving the {@link com.team23.weather.models.MarsForecast}
		 */
        @Override
        protected MarsForecast doInBackground() throws Exception {
        	
        	return _app.getWeatherData().getMarsWeather();
        	
        } //end doInBackground

        /**
         * Displays the new {@link com.team23.weather.models.MarsForecast} to the user.
         */
        @Override
        protected void done() {
        	
        	try {
        		
        		_forecast = get();
        		displayWeather(_forecast);
        		
        		} catch (Exception ignore) {
        			
        			ignore.printStackTrace();
        			
        		} //end catch
        	
            } //end try
        
        }.execute(); //end done
        
    } //end loadWeather
	
	/**
	 * Updates the display to the user with the most recent {@link com.team23.weather.models.MarsForecast}
	 * @param forecast A {@link com.team23.weather.models.MarsForecast} with the most recent weather
	 */
	private void displayWeather(MarsForecast forecast) {
        if(forecast == null) return;
		
		LocaleSettings localeSettings = _app.getConfiguration().getLocaleSettings();
        String iconUrl = forecast.getSkyConditionIconUrl();
		
		lblMarsSkyConditionImage.setIcon(iconUrl, 80, 80);
        lblMarsEarthDateData.setText(Formatter.asDate(forecast.getEarthDate()));
	    lblMarsDateData.setText(Integer.toString(forecast.getMarsDate()));
		lblMarsMaxMinTemperatureData.setText(forecast.getMaxTemp().toString(localeSettings) + "/" + forecast.getMinTemp().toString(localeSettings));
	    lblMarsPressureData.setText(forecast.getPressure().toString(localeSettings));	    	
	    lblMarsAbsoluteHumidityData.setText(String.format("%d%%", forecast.getAbsHumidity()));
	    lblMarsWindSpeedData.setText(forecast.getWindSpeed().toString(localeSettings));
	    lblMarsSkyConditionData.setText(forecast.getSkyCondition());
 
    } //end displayWeather

    /**
     * When the locale is changed, the display will refresh.
     * @param locale the {@link com.team23.weather.locale.LocaleSettings} containing the current units
     */
    @Override
    public void onLocaleChange(LocaleSettings locale) {
    	
        loadWeather();
        
    } //end onLocaleChange

    @Override
    public void beforeLocationChange(Location currentLocation) {
        // do nothing
    } //end beforeLocationChange

    /**
     * When the {@link com.team23.weather.models.Location} is changed, load the weather.
     */
    @Override
    public void onLocationChange(Location location) {
        loadWeather();
    } //end onLocationChange
    
} //end MarsForecastPanel
