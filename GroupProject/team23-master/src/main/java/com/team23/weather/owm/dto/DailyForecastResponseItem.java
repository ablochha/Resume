package com.team23.weather.owm.dto;

import com.team23.weather.locale.LocaleUnits;
import com.team23.weather.models.*;

import java.util.ArrayList;
import java.util.Date;

public class DailyForecastResponseItem {

    private long dt;
    private Temp temp;
    private double pressure;
    private int humidity;
    private final java.util.List<Weather> weather = new ArrayList<Weather>();
    private double speed;
    private int deg;
    private int clouds;
    private double snow;
    private double rain;

    public Forecast getForecast( LocaleUnits units ) {
        Forecast toReturn = new Forecast();

        toReturn.setGenerationTime(new Date(dt*1000));
        toReturn.setHumidity(humidity);
        // the API always returns metric pressure, even if you ask for imperial
        toReturn.setPressure(new Pressure(pressure, PressureUnit.Pascal));
        toReturn.setSkyCondition(new SkyCondition(
                weather.get(0).getMain(), // title
                weather.get(0).getDescription(), // description
                weather.get(0).getIcon() // icon
        ));
        toReturn.setWindVelocity(new Velocity(speed, deg, units.getSpeedUnit()));

        Temperature high = new Temperature(temp.getMax(), units.getTemperatureUnit());
        Temperature current = new Temperature(temp.getDay(), units.getTemperatureUnit());
        Temperature low = new Temperature(temp.getMin(), units.getTemperatureUnit());
        toReturn.setTemperature( new ForecastTemperature( high, current, low));

        return toReturn;
    }

    @Override
    public String toString() {
        return "DailyForecastResponseItem{" +
                "dt=" + dt +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", weather=" + weather +
                ", speed=" + speed +
                ", deg=" + deg +
                ", clouds=" + clouds +
                ", snow=" + snow +
                ", rain=" + rain +
                '}';
    }
}
