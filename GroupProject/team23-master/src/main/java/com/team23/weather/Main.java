package com.team23.weather;

import com.team23.weather.configuration.ConfigurationManager;
import com.team23.weather.configuration.IConfigurationManager;
import com.team23.weather.http.HttpClient;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.owm.IOwmClient;
import com.team23.weather.owm.OwmClient;
import com.team23.weather.mars.IMarsWeatherClient;
import com.team23.weather.mars.MarsWeatherClient;
import com.team23.weather.serialization.ISerializer;
import com.team23.weather.serialization.JsonSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.SwingUtilities;

/**
 * Static class for application start
 *
 * @author Saquib Mian
 */
public class Main {

    static Logger logger = LogManager.getLogger(Main.class.getName());

    /**
     * The main entry point into the app
     * This method initializes the backend on the main thread,
     * and then fires off the GUI on the EDT
     */
	public static void main(String[] args) {
        logger.info("Application start");

        /**
         * the web client
         */
        HttpClient httpClient = new HttpClient();

        /**
         * using the JSON serializer across the app
         */
        ISerializer serializer = new JsonSerializer();

        /**
         * the configuration manager, to handle persistence of user preferences
         */
        IConfigurationManager configurationManager = new ConfigurationManager(serializer);

        /**
         * the OWM cient, to get weather from
         */
        IOwmClient owmClient = new OwmClient(httpClient, serializer);
        owmClient.setLocale(configurationManager.load().getLocaleSettings());

        /**
         * the Mars weather client, to get Mars weather from
         */
        IMarsWeatherClient marsWeatherClient = new MarsWeatherClient(httpClient, serializer);

        /**
         * initialize the backend
         */
        App backend = new App(configurationManager, owmClient, marsWeatherClient);
        backend.registerObserver((ILocaleObserver)owmClient);

        /**
         * instantiate the {@link com.team23.weather.GuiRunner} which will
         * decide which GUI to run and invoke on the EDT
         */
        Runnable guiRunner = new GuiRunner( backend );
		SwingUtilities.invokeLater( guiRunner );
	}

}
