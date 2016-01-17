package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.CompoundForecast;
import com.team23.weather.models.Forecast;
import com.team23.weather.models.Location;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.ILocationObserver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * An HourlyForecastPanel extends {@link javax.swing.JPanel}. An
 * HourlyForecastPanel implements the
 * {@link com.team23.weather.observer.IWeatherDataObserver} interface, to listen
 * for weather changes.
 *
 * An HourlyForecastPanel implements the
 * {@link com.team23.weather.observer.ILocaleObserver} interface, to listen to
 * locale changes.
 *
 * An HourlyForecastPanel displays all the weather information from an hourly
 * {@link com.team23.weather.models.CompoundForecast} to the user. This includes
 * the time of the forecast, the {@link com.team23.weather.models.Temperature}
 * and the sky condition.
 *
 * @author Tim Whelan
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.models.CompoundForecast
 * @see com.team23.weather.models.WeatherData
 * @see javax.swing.JPanel
 * @see com.team23.weather.gui.HourlyForecastItemPanel
 *
 */

public class HourlyForecastPanel extends JPanel implements ILocationObserver, ILocaleObserver {

    /**
     * number of 3-hour segments to display in the forecast
     * defaulted to 8 segments (24 hours)
     */
    private final int _numForecastSegments = 8;

    /**
     * _app contains a reference to the backend of the program.
     */
    private final App _app;
    /**
     * _panels is the container for the individual forecasts
     */
    private final ArrayList<HourlyForecastItemPanel> _panels = new ArrayList<HourlyForecastItemPanel>();

    /**
     * Constructs a {@link javax.swing.JPanel} with {@link javax.swing.JLabel}'s
     * to display the information from a daily
     * {@link com.team23.weather.models.CompoundForecast}.
     *
     * @param app a reference to {@link com.team23.weather.App}
     */
    public HourlyForecastPanel(App app) {

        _app = app;
        _app.registerObserver((ILocaleObserver) this);
        _app.registerObserver((ILocationObserver) this);

        loadWeather();

    } //end ShortTermForecastPanel

    /**
     * Retrieves a {@link com.team23.weather.models.CompoundForecast} from
     * {@link com.team23.weather.models.WeatherData} and updates the display for
     * the user.
     */
    private void loadWeather() {
        new SwingWorker<CompoundForecast, Void>() {
            private CompoundForecast _forecast;

            @Override
            protected CompoundForecast doInBackground() throws Exception {
                return _app.getWeatherData().getHourlyForecast();
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
     * Updates the display to the user with the most recent hourly
     * {@link com.team23.weather.models.CompoundForecast}
     *
     * @param forecast A {@link com.team23.weather.models.CompoundForecast} with
     * upcoming weather
     */
    private void displayWeather(CompoundForecast forecast) {
        if (forecast == null) {
            return;
        }

        this.removeAll();

        LocaleSettings localeSettings = _app.getConfiguration().getLocaleSettings();
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout());

        _panels.clear();
        for (Forecast f : forecast.getForecasts()) {
            if (f.getGenerationTime().before(new Date())) {
                continue;
            }
            if( _panels.size() >= _numForecastSegments ) {
                continue;
            }

            HourlyForecastItemPanel toAdd = new HourlyForecastItemPanel();
            toAdd.setLocaleSettings(localeSettings);
            toAdd.setForecast(f);
            _panels.add(toAdd);
            containerPanel.add(toAdd);
        }

        this.add(containerPanel);
    }

    /**
     * When the {@link com.team23.weather.models.Location} is changed, load the
     * weather.
     */
    @Override
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
        for (HourlyForecastItemPanel panel : _panels) {
            panel.setLocaleSettings(locale);
        }
    }
} //end ShortTermForecastPanel
