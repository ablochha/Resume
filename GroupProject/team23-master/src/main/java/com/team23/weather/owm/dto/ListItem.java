
package com.team23.weather.owm.dto;

import com.team23.weather.locale.LocaleUnits;
import com.team23.weather.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class ListItem {

    private Integer id;
    private String name;
    private Coord coord;
    private Main main;
    private Integer dt;
    private Wind wind;
    private Sys sys;
    private Clouds clouds;
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    private Rain rain;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The coord
     */
    Coord getCoord() {
        return coord;
    }

    /**
     * 
     * @param coord
     *     The coord
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     * 
     * @return
     *     The main
     */
    Main getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * 
     * @return
     *     The dt
     */
    public Integer getDt() {
        return dt;
    }

    /**
     * 
     * @param dt
     *     The dt
     */
    public void setDt(Integer dt) {
        this.dt = dt;
    }

    /**
     * 
     * @return
     *     The wind
     */
    Wind getWind() {
        return wind;
    }

    /**
     * 
     * @param wind
     *     The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     * 
     * @return
     *     The sys
     */
    Sys getSys() {
        return sys;
    }

    /**
     * 
     * @param sys
     *     The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    /**
     * 
     * @return
     *     The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     * 
     * @param clouds
     *     The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     * 
     * @return
     *     The weather
     */
    java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    /**
     * 
     * @return
     *     The rain
     */
    public Rain getRain() {
        return rain;
    }

    /**
     * 
     * @param rain
     *     The rain
     */
    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    Location getLocation() {
        GeoCoordinate position = new GeoCoordinate(getCoord().getLat(), getCoord().getLon());
        Location toReturn = new Location(getId(), getName(), getSys().getCountry(), position);

        return toReturn;
    }

    public Forecast getForecast( LocaleUnits units ) {
        Forecast toReturn = new Forecast();

        toReturn.setGenerationTime(new Date());
        toReturn.setHumidity( getMain().getHumidity());
        toReturn.setLocationInformation(getLocation());
        toReturn.setPressure( new Pressure( getMain().getPressure(), units.getPressureUnit() ));
        toReturn.setSkyCondition(new SkyCondition(
                getWeather().get(0).getMain(), // title
                getWeather().get(0).getDescription(), // description
                getWeather().get(0).getIcon() // icon
        ));
        toReturn.setSunrise(new Date( getSys().getSunrise()));
        toReturn.setSunset(new Date( getSys().getSunset()));
        toReturn.setWindVelocity(new Velocity(getWind().getSpeed(), getWind().getDeg(), units.getSpeedUnit()));

        Temperature high = new Temperature(getMain().getTempMax(), units.getTemperatureUnit());
        Temperature current = new Temperature(getMain().getTemp(), units.getTemperatureUnit());
        Temperature low = new Temperature(getMain().getTempMin(), units.getTemperatureUnit());
        toReturn.setTemperature( new ForecastTemperature( high, current, low));

        return toReturn;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coord=" + coord +
                ", main=" + main +
                ", dt=" + dt +
                ", wind=" + wind +
                ", sys=" + sys +
                ", clouds=" + clouds +
                ", weather=" + weather +
                ", rain=" + rain +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

}
