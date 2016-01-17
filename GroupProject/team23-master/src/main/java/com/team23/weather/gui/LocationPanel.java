package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.formatting.Formatter;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.models.WeatherData;
import com.team23.weather.observer.ILocationObserver;
import com.team23.weather.observer.IWeatherDataObserver;
import com.team23.weather.swing.SwingWeatherDataUtilizer;

import javax.swing.*;
import java.awt.*;

/**
 * A LocationPanel extends {@link javax.swing.JPanel}.
 * A LocationPanel implements the {@link com.team23.weather.observer.IWeatherDataObserver}
 * interface, to listen for weather changes.
 * A LocationPanel implements the {@link com.team23.weather.observer.ILocationObserver}
 * interface, to listen to location changes.
 * 
 * A LocationPanel displays the current location for a weather forecast
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * 
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.RefreshAction
 * @see com.team23.weather.observer.ILocationObserver
 * @see com.team23.weather.observer.IWeatherDataObserver
 * @see javax.swing.JPanel
 *
 */
public class LocationPanel extends JPanel implements ILocationObserver, IWeatherDataObserver {

	/**
	 * _app contains a reference to the backend of the program.
	 */
    private final App _app;

    /**
     * lblCurrentLocation contains a reference to the city and country name for the forecast.
     */
    private final JLabel _lblCurrentLocation = new JLabel();

    /**
     * lblCurrentLocation contains a reference to the date of the forecast.
     */
    private final JLabel _lblDate = new JLabel();

    /**
     * _lblCurrentSkyConditionImage contains a reference to the forecasts icon indicating the sky condition.
     */
    private final LazyImageLabel _lblCurrentSkyConditionImage = new LazyImageLabel();

    /**
     * _lblUpdating contains a reference to the placeholder text before a location has been loaded.
     */
    private final JLabel _lblUpdating = new JLabel();

    /**
     * the left component in this panel (location and icon)
     */
    private JPanel _leftComponent = null;

    /**
     * the right component in this panel (date)
     */
    private JPanel _rightComponent = null;

    /**
     * Builds a new {@link com.team23.weather.gui.LocationPanel} with a reference to {@link com.team23.weather.App}.
     * @param app {@link com.team23.weather.App}, a reference to the back end of the program
     */
    public LocationPanel(App app)  {
    	
        _app = app;
        _app.registerObserver((ILocationObserver) this);
        _app.registerObserver((IWeatherDataObserver) this);

        _lblUpdating.setFont(Fonts.Small);

        _leftComponent = new JPanel();
        _leftComponent.setLayout(new FlowLayout(FlowLayout.LEFT));
        _leftComponent.add(_lblCurrentLocation);
        _leftComponent.add(_lblCurrentSkyConditionImage);
        _leftComponent.add(_lblUpdating);

        _rightComponent = new JPanel();
        _rightComponent.add(_lblDate);

        this.setLayout(new BorderLayout());
        this.add(_rightComponent, BorderLayout.EAST);
        this.add(_leftComponent, BorderLayout.WEST);

        showUpdating();

        displayLocation();
        displayWeatherInformation();
        
	} //end LocationPanel

    /**
     * Displays the placeholder text when a {@link com.team23.weather.models.Forecast} isn't loaded yet.
     */
    private void showUpdating() {
    	
        _lblUpdating.setText("(Updating...)");
        
    } //end showUpdating

    /**
     * Removes placeholder text.
     */
    private void removeUpdating() {
    	
        _lblUpdating.setText("");
        
    } //end removeUpdating

    /**
     * Updates the city and country name to the {@link com.team23.weather.models.Forecast}.
     */
    private void displayLocation() {
    	
        Location location = _app.getConfiguration().getCurrentLocation();
        if( location == null ){
            _lblCurrentLocation.setText("No location set");
        } else {
            _lblCurrentLocation.setText(location.getCity() + ", " + location.getCountry());
        }

        _lblCurrentLocation.setFont(Fonts.Large);
        
    } //end displayLocation

    /**
     * Updates the icon associated with the {@link com.team23.weather.models.SkyCondition}.
     */
    private void displayWeatherInformation() {
    	
        new SwingWeatherDataUtilizer(_app) {
            @Override
            protected void use(WeatherData data) {
                if( data !=null && data.hasLocationWeatherData() ) {
                    Forecast weather = data.getWeather();

                    _lblDate.setText(Formatter.asFullDate(weather.getGenerationTime()));
                    _lblDate.setFont(Fonts.Large);

                    String imageUrl = weather.getSkyCondition().getIconUrl();
                    _lblCurrentSkyConditionImage.setIcon(imageUrl);
                }
            }
        }.execute();
        
    } //end displayWeatherInformation

    @Override
    public void setBackground(Color c) {
        this.setOpaque(true);
        super.setBackground(c);

        if( _rightComponent != null ) {
            _rightComponent.setOpaque(true);
            _rightComponent.setBackground(c);
        }
        if( _leftComponent != null ){
            _leftComponent.setOpaque(true);
            _leftComponent.setBackground(c);
        }
    }

    /**
     * Display placeholder text while a {@link com.team23.weather.models.Forecast} is updating.
     */
    @Override
    public void beforeLocationChange(Location currentLocation) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showUpdating();
            }
        });
        
    } //end beforeLocationChange

    /**
     * Update the city and country name when the {@link com.team23.weather.models.Location} is changed.
     * @param location the new {@link com.team23.weather.models.Location}
     */
    @Override
    public void onLocationChange(Location location) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayLocation();
            }
        });
        
    } //end onLocationChange

    /**
     * Display placeholder text while the {@link com.team23.weather.models.Forecast} is updating.
     */
    @Override
    public void beforeWeatherDataUpdate(WeatherData weatherData) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showUpdating();
            }
        });
        
    } //end beforeWeatherDataUpdate

    /**
     * Remove placeholder text and display the new city and country names, and the icon for the {@link com.team23.weather.models.SkyCondition}.
     * @param weatherData the {@link com.team23.weather.models.WeatherData} holding all the information
     */
    @Override
    public void onWeatherDataUpdated(WeatherData weatherData) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeUpdating();
                displayWeatherInformation();
            }
        });
    } //end onWeatherDataUpdated
    
} //end LocationPanel