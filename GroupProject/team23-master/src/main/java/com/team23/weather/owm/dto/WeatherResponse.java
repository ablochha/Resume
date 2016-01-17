package com.team23.weather.owm.dto;

import com.team23.weather.locale.LocaleUnits;
import com.team23.weather.models.*;

import java.util.*;

public class WeatherResponse {

    private Coord coord;
    private Sys sys;
    private List<Weather> weather = new ArrayList<Weather>();
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Integer dt;
    private Integer id;
    private String name;
    private Integer cod;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The coord
     */
    Coord getCoord() {
        return coord;
    }

    /**
     *
     * @param coord
     * The coord
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     *
     * @return
     * The sys
     */
    Sys getSys() {
        return sys;
    }

    /**
     *
     * @param sys
     * The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    /**
     *
     * @return
     * The weather
     */
    List<Weather> getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The base
     */
    public String getBase() {
        return base;
    }

    /**
     *
     * @param base
     * The base
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     *
     * @return
     * The main
     */
    Main getMain() {
        return main;
    }

    /**
     *
     * @param main
     * The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @return
     * The wind
     */
    Wind getWind() {
        return wind;
    }

    /**
     *
     * @param wind
     * The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     *
     * @return
     * The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     *
     * @param clouds
     * The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     *
     * @return
     * The dt
     */
    public Integer getDt() {
        return dt;
    }

    /**
     *
     * @param dt
     * The dt
     */
    public void setDt(Integer dt) {
        this.dt = dt;
    }

    /**
     *
     * @return
     * The id
     */
    Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The cod
     */
    public Integer getCod() {
        return cod;
    }

    /**
     *
     * @param cod
     * The cod
     */
    public void setCod(Integer cod) {
        this.cod = cod;
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
        // the API always returns metric pressure, even if you ask for imperial
        toReturn.setPressure(new Pressure(getMain().getPressure(), PressureUnit.Pascal));
        toReturn.setSkyCondition(new SkyCondition(
                getWeather().get(0).getMain(), // title
                getWeather().get(0).getDescription(), // description
                getWeather().get(0).getIcon() // icon
        ));
        toReturn.setSunrise(new Date(getSys().getSunrise()*1000));
        toReturn.setSunset(new Date( getSys().getSunset()*1000));
        toReturn.setWindVelocity(new Velocity(getWind().getSpeed(), getWind().getDeg(), units.getSpeedUnit()));

        Temperature high = new Temperature(getMain().getTempMax(), units.getTemperatureUnit());
        Temperature current = new Temperature(getMain().getTemp(), units.getTemperatureUnit());
        Temperature low = new Temperature(getMain().getTempMin(), units.getTemperatureUnit());
        toReturn.setTemperature( new ForecastTemperature( high, current, low));

        return toReturn;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "coord=" + coord +
                ", sys=" + sys +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}