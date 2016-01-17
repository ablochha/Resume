package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.formatting.Formatter;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.ILocationObserver;

import javax.swing.*;
import java.awt.*;

/**
 * A CurrentForecastPanel extends {@link javax.swing.JPanel}.
 * A CurrentForecastPanel implements the {@link com.team23.weather.observer.ILocaleObserver}
 * interface, to listen for {@link com.team23.weather.locale.LocaleSettings} changes.
 * A CurrentForecastPanel implements the {@link com.team23.weather.observer.ILocationObserver}
 * interface, to listen to {@link com.team23.weather.models.Location} changes.
 * 
 * A CurrentForecastPanel displays the current forecast for a {@link com.team23.weather.models.Location}.
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * 
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.RefreshAction
 * @see com.team23.weather.observer.ILocaleObserver
 * @see com.team23.weather.observer.ILocationObserver
 * @see javax.swing.JPanel
 *
 */
public class CurrentForecastPanel extends JPanel implements ILocationObserver, ILocaleObserver {

	/**
	 * _app contains a reference to the back end of the program.
	 */
    private final App _app;

    /**
     * _lblCurrentTemperatureData contains the temperature from the current forecast.
     */
    private final JLabel _lblCurrentTemperatureData;
    
    /**
     * _lblCurrentWindSpeedData contains the wind speed from the current forecast.
     */
    private final JLabel _lblCurrentWindSpeedData;
    
    /**
     * _lblCurrentAirePressureData contains the air pressure from the current forecast.
     */
    private final JLabel _lblCurrentAirPressureData;
    
    /**
     * _lblCurrentHumidityData contains the humidity from the current forecast.
     */
    private final JLabel _lblCurrentHumidityData;
    
    /**
     * _lblCurrentSkyConditionData contains the sky condition from the current forecast.
     */
    private final JLabel _lblCurrentSkyConditionData;
    
    /**
     * _lblCurrentExpectedMaxMinTempData contains the Max/Min temperature from the current forecast.
     */
    private final JLabel _lblCurrentExpectedMaxMinTempData;
    
    /**
     * _lblCurrentSunriseSunsetData contains the sunrise/sunset from the current forecast.
     */
    private final JLabel _lblCurrentSunriseSunsetData;
    
    /**
     * _lblSkyConditionImage contains the icon depicting the sky condition from the current forecast.
     */
    private final LazyImageLabel _lblSkyConditionImage;
    
    
    /**
     * Construct a {@link com.team23.weather.gui.CurrentForecastPanel}.
     * @param app {@link com.team23.weather.App}, a reference to the back end of the program
     */
    public CurrentForecastPanel(App app) {
    	
        _app = app;
        _app.registerObserver((ILocaleObserver) this);
        _app.registerObserver((ILocationObserver) this);

        _lblCurrentTemperatureData = new JLabel();
        _lblCurrentWindSpeedData = new JLabel();
        _lblCurrentAirPressureData = new JLabel();
        _lblCurrentHumidityData = new JLabel();
        _lblCurrentSkyConditionData = new JLabel();
        _lblCurrentExpectedMaxMinTempData = new JLabel();
        _lblCurrentSunriseSunsetData = new JLabel();
        _lblSkyConditionImage = new LazyImageLabel();

        _lblCurrentTemperatureData.setFont(Fonts.Large);
        _lblCurrentWindSpeedData.setFont(Fonts.Medium);
        _lblCurrentAirPressureData.setFont(Fonts.Medium);
        _lblCurrentHumidityData.setFont(Fonts.Medium);
        _lblCurrentSkyConditionData.setFont(Fonts.Medium);
        _lblCurrentExpectedMaxMinTempData.setFont(Fonts.Medium);
        _lblCurrentSunriseSunsetData.setFont(Fonts.Medium);

        final float alignment = Component.CENTER_ALIGNMENT;
        JPanel labelForWindSpeedAndDirection = new JComponentLabel("Wind Speed & Direction", _lblCurrentWindSpeedData, alignment);
        JPanel labelForHumidity = new JComponentLabel("Humidity", _lblCurrentHumidityData, alignment);
        JPanel labelForAirPressure = new JComponentLabel("Air Pressure", _lblCurrentAirPressureData, alignment);
        JPanel labelSunriseAndSunset = new JComponentLabel("Sunrise/Sunset", _lblCurrentSunriseSunsetData, alignment);
        Component topFiller = Box.createVerticalStrut(50);
        Component middleFiller = Box.createVerticalStrut(10);

        GroupLayout l = new GroupLayout(this);
        l.setAutoCreateGaps(true);
        l.setVerticalGroup(
                l.createSequentialGroup()
                        .addComponent(topFiller)
                        .addGroup(
                                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(_lblSkyConditionImage)
                                        .addComponent(_lblCurrentTemperatureData)
                        )
                        .addGroup(
                                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(_lblCurrentSkyConditionData)
                                        .addComponent(_lblCurrentExpectedMaxMinTempData)
                        )
                        .addComponent(middleFiller)
                        .addGroup(
                                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(labelForHumidity)
                                        .addComponent(labelForAirPressure)
                        )
                        .addComponent(labelSunriseAndSunset)
                        .addComponent(labelForWindSpeedAndDirection)
        );
        l.setHorizontalGroup(
                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(topFiller)
                        .addGroup(
                                l.createSequentialGroup()
                                        .addComponent(_lblSkyConditionImage)
                                        .addComponent(_lblCurrentTemperatureData)
                        )
                        .addGroup(
                                l.createSequentialGroup()
                                        .addComponent(_lblCurrentSkyConditionData)
                                        .addComponent(_lblCurrentExpectedMaxMinTempData)
                        )
                        .addComponent(middleFiller)
                        .addGroup(
                                l.createSequentialGroup()
                                        .addComponent(labelForHumidity)
                                        .addComponent(labelForAirPressure)
                        )
                        .addComponent(labelSunriseAndSunset)
                        .addComponent(labelForWindSpeedAndDirection)
        );

        this.setLayout(l);

        loadWeather();

    } //end CurrentForecastPanel

    /**
	 * Retrieves a {@link com.team23.weather.models.Forecast} from {@link com.team23.weather.models.WeatherData}
	 * and updates the display for the user.
	 */
    private void loadWeather() {
    	
        new SwingWorker<Forecast, Void>() {
            private Forecast _forecast;

            @Override
            protected Forecast doInBackground() throws Exception {
                return _app.getWeatherData().getWeather();
            }

            @Override
            protected void done() {
                try {
                    _forecast = get();
                    if(_forecast != null) {
                        displayWeather(_forecast);
                    } //end if
                } //end try
                
                catch (Exception ignore) {
                    ignore.printStackTrace();
                } //end catch
                
            } //end done
        }.execute(); //end SwingWorker
        
    } //end loadWeather

    /**
	 * Updates the display to the user with the most recent {@link com.team23.weather.models.Forecast}
	 * @param forecast A {@link com.team23.weather.models.Forecast} with the most recent weather
	 */
    private void displayWeather(Forecast forecast) {
    	
        LocaleSettings localeSettings = _app.getConfiguration().getLocaleSettings();

        _lblSkyConditionImage.setIcon(forecast.getSkyCondition().getIconUrl());
        _lblCurrentTemperatureData.setText(forecast.getTemperature().getCurrent().toString(localeSettings));
        _lblCurrentWindSpeedData.setText(forecast.getWindVelocity().toString(localeSettings));
        _lblCurrentAirPressureData.setText(forecast.getPressure().toString(localeSettings));
        _lblCurrentHumidityData.setText(Double.toString(forecast.getHumidity()) + "%");
        _lblCurrentSkyConditionData.setText(forecast.getSkyCondition().getTitle());
        _lblCurrentExpectedMaxMinTempData.setText(forecast.getTemperature().getLow().toString(localeSettings) + "/" + forecast.getTemperature().getHigh().toString(localeSettings));
        _lblCurrentSunriseSunsetData.setText(Formatter.asTime(forecast.getSunrise()) + "/" + Formatter.asTime(forecast.getSunset()));
        
    } //end displayWeather

    @Override
    public void beforeLocationChange(Location currentLocation) {
        // show loading stuffs here
    }

    /**
     * Updates the display when the {@link com.team23.weather.models.Location} changes.
     * @param location the new {@link com.team23.weather.models.Location}
     */
    @Override
    public void onLocationChange(Location location) {
        loadWeather();
    } //end onLocationChange

    /**
     * Updates the display when the {@link com.team23.weather.locale.LocaleSettings} change.
     * @param locale the new {@link com.team23.weather.locale.LocaleSettings} 
     */
    @Override
    public void onLocaleChange(LocaleSettings locale) {
        loadWeather();
    } //end onLocaleChange
    
} //end CurrentForecastPanel
