package com.team23.weather.models;

/**
 * This model represents the location on the globe.
 *
 * @author Saquib Mian
 */
public class Location {

    /**
     * the location ID as represented in OWM's domain, or any other external domain.
     */
    private final int _LocationId;

    /**
     * The name of the city
     */
    private String _city;

    /**
     * The name of the country
     */
    private String _country;

    /**
     * The coordinate on the map
     */
    private GeoCoordinate _position;

    /**
     * The basic weather of the location
     */
    private Forecast _weather;

    /**
     *
     * @param locationId the location ID as represented in OWM's domain, or any other external domain.
     * @param city the name of the city
     * @param country the name of the country
     * @param position the coordinate on the map
     */
    public Location(int locationId, String city, String country, GeoCoordinate position) {
        _LocationId = locationId;
        _city = city;
        _country = country;
        _position = position;
    }

    /**
     * get the location ID
     * @return the location ID
     */
    public int getLocationId() {
        return _LocationId;
    }

    /**
     * get the city's name
     * @return the city's name
     */
    public String getCity() {
        return _city;
    }

    /**
     * set the name of the city
     * @param city the new name
     */
    public void setCity(String city) {
        _city = city;
    }

    /**
     * get the country's name
     * @return the country's name
     */
    public String getCountry() {
        return _country;
    }

    /**
     * set the name of the country
     * @param country the new name
     */
    public void setCountry(String country) {
        _country = country;
    }

    /**
     * get the location on the map
     * @return the location on the map
     */
    public GeoCoordinate getPosition() {
        return _position;
    }

    /**
     * set the position
     * @param position the new position
     */
    public void setPosition(GeoCoordinate position) {
        _position = position;
    }

    public Forecast getWeather() {
        return _weather;
    }

    public void setWeather(Forecast weather) {
        _weather = weather;
    }
}
