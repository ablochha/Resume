package com.team23.weather.owm.dto;

import com.team23.weather.models.GeoCoordinate;
import com.team23.weather.models.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HourlyForecastResponse {

    private String cod;
    private Double message;
    private City city;
    private Integer cnt;
    private java.util.List<HourlyForecastResponseItem> list = new ArrayList<HourlyForecastResponseItem>();
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The cod
     */
    public String getCod() {
        return cod;
    }

    /**
     *
     * @param cod
     * The cod
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     *
     * @return
     * The message
     */
    public Double getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(Double message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The city
     */
    City getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     *
     * @param cnt
     * The cnt
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    /**
     *
     * @return
     * The list
     */
    public java.util.List<HourlyForecastResponseItem> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<HourlyForecastResponseItem> list) {
        this.list = list;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Location getLocation() {
        GeoCoordinate position = new GeoCoordinate(getCity().getCoord().getLat(), getCity().getCoord().getLon());
        Location toReturn = new Location(getCity().getId(), getCity().getName(), getCity().getCountry(), position);

        return toReturn;
    }

    @Override
    public String toString() {
        return "HourlyForecastResponse{" +
                "cod='" + cod + '\'' +
                ", message=" + message +
                ", city=" + city +
                ", cnt=" + cnt +
                ", list=" + list +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}