package com.team23.weather.observer;

import com.team23.weather.models.Location;

/**
 * Using this interface, an object can handle changes in the {@link com.team23.weather.models.Location} model.
 * Changes to the {@link com.team23.weather.models.Location} model are appropriated by {@link com.team23.weather.App}.
 *
 * @author Saquib Mian
 */
public interface ILocationObserver {
    /**
     * Before the {@link com.team23.weather.models.Location} object changes
     * @param currentLocation the previous representation of the {@link com.team23.weather.models.Location} object
     */
    void beforeLocationChange(Location currentLocation);

    /**
     * After the {@link com.team23.weather.models.Location} object has changed
     * @param location the new representation of the {@link com.team23.weather.models.Location} object
     */
    void onLocationChange(Location location);
}
