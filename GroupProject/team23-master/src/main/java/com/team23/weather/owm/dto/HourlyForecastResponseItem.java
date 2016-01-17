package com.team23.weather.owm.dto;

import com.team23.weather.locale.LocaleUnits;
import com.team23.weather.models.*;

import java.util.ArrayList;
import java.util.Date;

public class HourlyForecastResponseItem {

    private long dt;
    private Main main;
    private final java.util.List<Weather> weather = new ArrayList<Weather>();
    private Clouds clouds;
    private Wind wind;
    private Sys sys;
    private String dtTxt;
    private Snow snow;
    private Rain rain;
    
    public Forecast getForecast( LocaleUnits units ) {
        Forecast toReturn = new Forecast();

        toReturn.setGenerationTime(new Date(dt*1000));
        toReturn.setHumidity(main.getHumidity());
        // the API always returns metric pressure, even if you ask for imperial
        toReturn.setPressure( new Pressure( main.getPressure(), PressureUnit.Pascal ));
        toReturn.setSkyCondition(new SkyCondition(
                weather.get(0).getMain(), // title
                weather.get(0).getDescription(), // description
                weather.get(0).getIcon() // icon
        ));
        toReturn.setSunrise(new Date( sys.getSunrise()*1000));
        toReturn.setSunset(new Date( sys.getSunset()*1000));
        toReturn.setWindVelocity(new Velocity(wind.getSpeed(), wind.getDeg(), units.getSpeedUnit()));

        Temperature high = new Temperature(main.getTempMax(), units.getTemperatureUnit());
        Temperature current = new Temperature(main.getTemp(), units.getTemperatureUnit());
        Temperature low = new Temperature(main.getTempMin(), units.getTemperatureUnit());
        toReturn.setTemperature( new ForecastTemperature( high, current, low));

        return toReturn;
    }

    @Override
    public String toString() {
        return "HourlyForecastResponseItem{" +
                "dt=" + dt +
                ", main=" + main +
                ", weather=" + weather +
                ", clouds=" + clouds +
                ", wind=" + wind +
                ", sys=" + sys +
                ", dtTxt='" + dtTxt + '\'' +
                ", snow=" + snow +
                ", rain=" + rain +
                '}';
    }
}