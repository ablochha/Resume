package com.team23.weather.configuration;

import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.*;

import java.util.Date;

/**
 * The main persisted configuration model
 * This model holds the last saved location as well as any
 * relevant preferences, like {@link com.team23.weather.locale.LocaleSettings}.
 *
 * @author Saquib Mian
 * @see com.team23.weather.locale.LocaleSettings
 * @see com.team23.weather.models.Location
 */
public class Configuration {

    /**
     * the last saved location
     */
    private Location _currentLocation;

    /**
     * the date last saved
     */
    private Date _lastSaved;

    /**
     * the locale settings
     */
    private LocaleSettings _localeSettings;

    /**
     * This serves up the default configuration model when the application first starts, with no location and Metric locale.
     * @return the default configuration model
     */
    public static Configuration getDefaults() {
        Configuration toReturn = new Configuration();

        LocaleSettings localeSettings = LocaleSettings.getMetricSettings();
        toReturn.setLocaleSettings(localeSettings);

        return toReturn;
    }

    /**
     * Get the current {@link com.team23.weather.models.Location}
     * @return the current {@link com.team23.weather.models.Location}
     */
    public Location getCurrentLocation() {
        return _currentLocation;
    }

    /**
     * Set the current {@link com.team23.weather.models.Location}
     * @param currentLocation the current {@link com.team23.weather.models.Location}
     */
    public void setCurrentLocation(Location currentLocation) {
        _currentLocation = currentLocation;
    }

    /**
     * Get the date last saved
     * @return the date last saved
     */
    public Date getLastSaved() {
        return _lastSaved;
    }

    /**
     * Set the date last saved
     * @param lastSaved the date last saved
     */
    public void setLastSaved(Date lastSaved) {
        _lastSaved = lastSaved;
    }

    /**
     * Get the current {@link com.team23.weather.locale.LocaleSettings}
     * @return the current {@link com.team23.weather.locale.LocaleSettings}
     */
    public LocaleSettings getLocaleSettings() {
        return _localeSettings;
    }

    /**
     * Set the current {@link com.team23.weather.locale.LocaleSettings}
     * @param localeSettings the current {@link com.team23.weather.locale.LocaleSettings}
     */
    public void setLocaleSettings(LocaleSettings localeSettings) {
        _localeSettings = localeSettings;
    }
}
