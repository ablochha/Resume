package com.team23.weather.configuration;

/**
 * The interface that any configuration persistence layer should implement,
 * with methods to save/read the {@link com.team23.weather.configuration.Configuration}
 * object from the backing store.
 *
 * @author Saquib Mian
 */
public interface IConfigurationManager {

    /**
     * Reads the configuration from the backing store and loads it.
     * @return the last saved {@link com.team23.weather.configuration.Configuration} object
     */
    Configuration load();

    /**
     * Persists the {@link com.team23.weather.configuration.Configuration} object to the backing store
     * @param config the {@link com.team23.weather.configuration.Configuration} object to save
     */
    void save(Configuration config);

}
