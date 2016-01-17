package com.team23.weather.observer;

import com.team23.weather.locale.LocaleSettings;

/**
 * Using this interface, an object can handle changes in the {@link com.team23.weather.locale.LocaleSettings} model.
 * Changes to the {@link com.team23.weather.locale.LocaleSettings} model are appropriated by {@link com.team23.weather.App}.
 *
 * @author Saquib Mian
 */
public interface ILocaleObserver {
    /**
     * After the {@link com.team23.weather.locale.LocaleSettings} object has changed
     * @param locale the new representation of the {@link com.team23.weather.locale.LocaleSettings} object
     */
    void onLocaleChange(LocaleSettings locale);
}
