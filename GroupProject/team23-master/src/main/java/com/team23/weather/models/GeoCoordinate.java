package com.team23.weather.models;

/**
 * Represents a 2-dimension coordinate for the world.
 *
 * @author Saquib Mian
 */
public class GeoCoordinate {

    /**
     * the latitude
     */
    private double _latitude;

    /**
     * the longitude
     */
    private double _longitude;

    /**
     *
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public GeoCoordinate(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    /**
     * get the latitude
     * @return the latitude
     */
    public double getLatitude() {
        return _latitude;
    }

    /**
     * set the latitude
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        _latitude = latitude;
    }

    /**
     * get the longitude
     * @return the longitude
     */
    public double getLongitude() {
        return _longitude;
    }

    /**
     * set the longitude
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        _longitude = longitude;
    }
}
