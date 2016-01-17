package com.team23.weather.swing;

import com.team23.weather.App;
import com.team23.weather.models.WeatherData;

import javax.swing.*;

/**
 * This class implements {@link javax.swing.SwingWorker} to provide a way to use
 * a {@link com.team23.weather.models.WeatherData} object on the Swing Event Dispatch
 * Thread. It does the expensive work on a worker thread, and then executes the
 * use() method on the EDT
 *
 * Implement your Swing-specific logic in the use() method.
 *
 * @author Saquib Mian
 */
public abstract class SwingWeatherDataUtilizer extends SwingWorker<WeatherData,Void> {
    /**
     * The backend
     */
    private final App _app;

    public SwingWeatherDataUtilizer(App app) {
        _app = app;
    }

    /**
     * Gets the weather data
     */
    @Override
    protected WeatherData doInBackground() throws Exception {
        return _app.getWeatherData();
    }

    /**
     * Passes control to use()
     */
    @Override
    protected void done() {
        try {
            use(get());
        } catch (Exception e) {
            e.printStackTrace();
            use(null);
        }
    }

    /**
     * Implement this method to perform any Swing-related work with the {@link com.team23.weather.models.WeatherData} object
     * @param data the {@link com.team23.weather.models.WeatherData} model obtained from the backend
     */
    protected abstract void use(WeatherData data);
}
