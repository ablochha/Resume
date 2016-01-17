package com.team23.weather.gui;

import com.team23.weather.formatting.Formatter;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Forecast;

import javax.swing.*;

/**
 * A HourlyForecastItemPanel extends {@link javax.swing.JPanel}. A
 * HourlyForecastItemPanel displays information about the weather from a
 * {@link com.team23.weather.models.Forecast} to the user. This includes the
 * date of the forecast, the minimum and maximum
 * {@link com.team23.weather.models.Temperature}'s, and the sky condition.
 *
 * A HourlyForecastItemPanel updates through the HourlyForecastPanel
 *
 * @author Tim
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.models.Forecast
 * @see com.team23.weather.models.WeatherData
 * @see javax.swing.JPanel
 * @see com.team23.weather.gui.HourlyForecastPanel
 *
 */
class HourlyForecastItemPanel extends JPanel {

    /**
     * lblShortTermTimeDate contains the time of the forecast
     */
    private final JLabel lblShortTermTimeData;
    /**
     * lblShortTermTemperatureData contains the expected temperature
     */
    private final JLabel lblShortTermTemperatureData;
    /**
     * lblShortTermSkyConditionData contains a description of the sky condition
     */
    private final JLabel lblShortTermSkyConditionData;
    /**
     * lblShortTermSkyConditionImage contains an icon of the sky condition
     */
    private final LazyImageLabel lblShortTermSkyConditionImage;
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
     * to display the information from a one
     * {@link com.team23.weather.models.Forecast}.
     */

    public HourlyForecastItemPanel() {

        lblShortTermTimeData = new JLabel();
        lblShortTermTemperatureData = new JLabel();
        lblShortTermSkyConditionData = new JLabel();
        lblShortTermSkyConditionImage = new LazyImageLabel();

        lblShortTermTemperatureData.setFont(Fonts.Large);
        lblShortTermTimeData.setFont(Fonts.Medium);

        GroupLayout l = new GroupLayout(this);
        l.setHorizontalGroup(
                l.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(
                        l.createSequentialGroup()
                        .addComponent(lblShortTermSkyConditionImage)
                        .addComponent(lblShortTermTemperatureData)
                )
                .addComponent(lblShortTermSkyConditionData)
                .addComponent(lblShortTermTimeData)
        );
        l.setVerticalGroup(
                l.createSequentialGroup()
                .addGroup(
                        l.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(lblShortTermSkyConditionImage)
                        .addComponent(lblShortTermTemperatureData)
                )
                .addComponent(lblShortTermSkyConditionData)
                .addComponent(lblShortTermTimeData)
        );
        this.setLayout(l);

    }

    /**
     * Changes the panel's forecast item and calls displayForecast() to update
     * labels
     *
     * @param forecast a reference to the new forecast
     */
    void setForecast(Forecast forecast) {
        _forecast = forecast;
        displayForecast();
    }

    /**
     * Change the units of display
     *
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
        if (_localeSettings == null || _forecast == null) {
            return;
        }

        lblShortTermTimeData.setText(Formatter.asTime(_forecast.getGenerationTime()));
        lblShortTermTemperatureData.setText(_forecast.getTemperature().getCurrent().toString(_localeSettings));
        lblShortTermSkyConditionData.setText(_forecast.getSkyCondition().getTitle());
        lblShortTermSkyConditionImage.setIcon(_forecast.getSkyCondition().getIconUrl());
        
    }

}
