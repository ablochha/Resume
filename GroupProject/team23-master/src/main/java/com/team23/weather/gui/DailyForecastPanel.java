package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.CompoundForecast;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.ILocationObserver;

import javax.swing.*;
import java.util.ArrayList;
/**
 * A DailyForecastPanel extends {@link javax.swing.JPanel}.
 * A DailyForecastPanel implements the {@link com.team23.weather.observer.IWeatherDataObserver}
 * interface, to listen for weather changes.
 * 
 * A DailyForecastPanel implements the {@link com.team23.weather.observer.ILocaleObserver}
 * interface, to listen to locale changes.
 * 
 * A DailyForecastPanel displays all the weather information from a daily {@link com.team23.weather.models.CompoundForecast}
 * to the user. This includes the date of the forecast, 
 * the minimum and maximum {@link com.team23.weather.models.Temperature}'s, and 
 * the sky condition. 
 * 
 * @author Tim Whelan
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.models.CompoundForecast
 * @see com.team23.weather.models.WeatherData
 * @see javax.swing.JPanel
 * @see com.team23.weather.gui.DailyForecastItemPanel
 *
 */
public class DailyForecastPanel extends JPanel implements ILocationObserver, ILocaleObserver {
     /**
     * _app contains a reference to the backend of the program.
     */
    private final App _app;
     /**
     * _panels is the container for the individual forecasts
     */
    private final ArrayList<DailyForecastItemPanel> _panels = new ArrayList<DailyForecastItemPanel>();

    /**
     * Constructs a {@link javax.swing.JPanel} with {@link javax.swing.JLabel}'s
     * to display the information from a daily {@link com.team23.weather.models.CompoundForecast}.
     * @param app a reference to {@link com.team23.weather.App}
     */
    public DailyForecastPanel(App app) {
        
        _app = app;
        _app.registerObserver((ILocaleObserver) this);
        _app.registerObserver((ILocationObserver) this);

        //Labels used in table for long term forecast
        JLabel lblLongTermWeather = new JLabel("Long Term Forecast");
        lblLongTermWeather.setFont(Fonts.Large);

        loadWeather();

    } //end LongTermForecastPanel
         /**
	 * Retrieves a {@link com.team23.weather.models.CompoundForecast} from {@link com.team23.weather.models.WeatherData}
	 * and updates the display for the user.
	 */
    private void loadWeather() {
        new SwingWorker<CompoundForecast, Void>() {
            private CompoundForecast _forecast;

            @Override
            protected CompoundForecast doInBackground() throws Exception {
                return _app.getWeatherData().getDailyForecast();
            }

            @Override
            protected void done() {
                try {
                    _forecast = get();
                    displayWeather(_forecast);
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        }.execute();
    }
	/**
	 * Updates the display to the user with the most recent daily {@link com.team23.weather.models.CompoundForecast}
	 * @param forecast A {@link com.team23.weather.models.CompoundForecast} with upcoming weather
	 */
    private void displayWeather(CompoundForecast forecast) {
        if(forecast == null) return;

        this.removeAll();

        LocaleSettings localeSettings = _app.getConfiguration().getLocaleSettings();
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        _panels.clear();
        for(Forecast f : forecast.getForecasts()) {
            DailyForecastItemPanel toAdd = new DailyForecastItemPanel();
            toAdd.setLocaleSettings(localeSettings);
            toAdd.setForecast(f);
            _panels.add(toAdd);
            containerPanel.add(toAdd);
        }

        this.add(containerPanel);
    }
     /**
     * When the {@link com.team23.weather.models.Location} is changed, load the weather.
     */
    public void onLocationChange(Location location) {
        loadWeather();
    }

    public void beforeLocationChange(Location currentLocation) {
       // show loading stuffs here
    }
    /**
     * When the locale is changed, the display will refresh.
     *
     * @param locale the {@link com.team23.weather.locale.LocaleSettings}
     * containing the current units
     */
    @Override
    public void onLocaleChange(LocaleSettings locale) {
        for(DailyForecastItemPanel panel : _panels) {
            panel.setLocaleSettings(locale);
        }
        loadWeather();
    }
} //end LongTermForecastPanel
