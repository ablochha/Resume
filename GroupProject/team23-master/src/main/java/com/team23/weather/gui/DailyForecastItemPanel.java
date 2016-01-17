/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team23.weather.gui;

import com.team23.weather.formatting.Formatter;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Forecast;

import java.awt.*;
import javax.swing.*;

/**
 * A DailyForecastItemPanel extends {@link javax.swing.JPanel}.
 * A DailyForecastItemPanel displays information about the weather from a {@link com.team23.weather.models.Forecast}
 * to the user. This includes the date of the forecast, 
 * the minimum and maximum {@link com.team23.weather.models.Temperature}'s, and 
 * the sky condition. 
 * 
 * A DailyForecastItemPanel updates through the DailyForecastPanel
 * 
 * @author Tim
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.models.Forecast
 * @see com.team23.weather.models.WeatherData
 * @see javax.swing.JPanel
 * @see com.team23.weather.gui.DailyForecastPanel
 * 
 */
class DailyForecastItemPanel extends JPanel {
    /** 
     * lblLongTermTimeDate contains the date of the forecast
     */
    private final JLabel lblLongTermTimeData;
    /**
     * lblLongTermTemperatureData contains the expected temperature 
     */
    private final JLabel lblLongTermTemperatureData;		
    /**
     * lblLongTermMaxMinData contains the maximum and minimum temperature of the forecast
     */
    private final JLabel lblLongTermMaxMinData;	
    /**
     * lblLongTermSkyConditionData contains a description of the sky condition
     */
    private final JLabel lblLongTermSkyConditionData;	
    /**
     * lblLongTermSkyConditionImage contains an icon of the sky condition
     */
    private final LazyImageLabel lblLongTermSkyConditionImage;
    /**
     * _forecast is the object to update data from
     */
    private Forecast _forecast;
    /**
     * _localeSettings contains information about units
     */
    private LocaleSettings _localeSettings;
    /**
     * Constructs a {@link javax.swing.JPanel} with {@link javax.swing.JLabel}'s
     * to display the information from a one {@link com.team23.weather.models.Forecast}.
     */
    public DailyForecastItemPanel() {
     
        lblLongTermTimeData = new JLabel();
        lblLongTermTemperatureData = new JLabel();
        lblLongTermSkyConditionData = new JLabel();
        lblLongTermMaxMinData = new JLabel();
        lblLongTermSkyConditionImage = new LazyImageLabel();

        lblLongTermTimeData.setFont(Fonts.Medium);
        lblLongTermTemperatureData.setFont(Fonts.Medium);
        lblLongTermSkyConditionData.setFont(Fonts.Medium);
        lblLongTermMaxMinData.setFont(Fonts.Medium);
        lblLongTermSkyConditionImage.setFont(Fonts.Medium);

        this.setLayout(new GridLayout(1, 5));
        this.add(lblLongTermTimeData);
        this.add(lblLongTermSkyConditionData);
        this.add(lblLongTermSkyConditionImage);
        this.add(lblLongTermTemperatureData);
        this.add(lblLongTermMaxMinData);

    }
    /**
     * Changes the panel's forecast item and calls displayForecast() to update labels
     * @param forecast a reference to the new forecast
     */
    void setForecast(Forecast forecast) {
        _forecast = forecast;
        displayForecast();

    }
    /**
     * Change the units of display
     * @param localeSettings is the unit set to use
     */
    public void setLocaleSettings(LocaleSettings localeSettings) {
        _localeSettings = localeSettings;
        displayForecast();
    }
    /**
     * Using a new forecast update labels
     */
    private void displayForecast() {
        if( _localeSettings == null || _forecast == null ) return;

        lblLongTermTimeData.setText(Formatter.asDate(_forecast.getGenerationTime()));
        lblLongTermTemperatureData.setText(_forecast.getTemperature().getCurrent().toString(_localeSettings));
        lblLongTermSkyConditionData.setText(_forecast.getSkyCondition().getTitle());
        lblLongTermMaxMinData.setText(_forecast.getTemperature().getLow().toString(_localeSettings) + "/" + _forecast.getTemperature().getHigh().toString(_localeSettings));
        lblLongTermSkyConditionImage.setIcon(_forecast.getSkyCondition().getIconUrl());

    }


}