package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.formatting.Formatter;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.WeatherData;
import com.team23.weather.observer.ILocaleObserver;
import com.team23.weather.observer.IWeatherDataObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * A ChangeUnitsPanel extends {@link javax.swing.JPanel}.
 * A ChangeUnitsPanel implements the {@link com.team23.weather.observer.IWeatherDataObserver}
 * interface, to listen for weather changes.
 * A ChangeUnitsPanel implements the {@link com.team23.weather.observer.ILocaleObserver}
 * interface, to listen to locale changes.
 * 
 * A ChangeUnitsPanel displays the date the weather was last updated, and allows the user
 * to change the {@link com.team23.weather.locale.LocaleSettings} to "Metric" or "Imperial".
 * Changing these settings will write the {@link com.team23.weather.configuration.Configuration} to a file
 * called "config.json". 
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * 
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.RefreshAction
 * @see com.team23.weather.observer.ILocaleObserver
 * @see com.team23.weather.observer.IWeatherDataObserver
 * @see javax.swing.JPanel
 *
 */
public class ChangeUnitsPanel extends JPanel implements ILocaleObserver, IWeatherDataObserver {

	/**
	 * _lblLastUpdated contains the date and time that the weather was last updated.
	 */
    private final JLabel _lblLastUpdated;	
    
    /**
     * _radMetric contains a reference to the radio button "Metric".
     */
    private final JRadioButton _radMetric;
    
    /**
     * _radImperial contains a reference to the radio button "Imperial".
     */
    private final JRadioButton _radImperial;		//Radial button to choose units

    /**
     * Creates a {@link javax.swing.JPanel} with a "Refresh" button and "Metric" and "Imperial" {@link javax.swing.JRadioButton}'s.
     * @param app {@link com.team23.weather.App}, a reference to the back end of the program
     */
    public ChangeUnitsPanel( App app ) {
    	
        app.registerObserver((ILocaleObserver) this);
        app.registerObserver((IWeatherDataObserver) this);

        JPanel panelBlankSpace;
		JButton btnRefresh;				//Button to refresh the weather forecast data
		ButtonGroup grpUnits;			//Grouping for radial buttons
		
		//Labels and buttons for refresh, units
		panelBlankSpace = new JPanel();
		_lblLastUpdated = new JLabel("The weather was last updated at: never");
		_lblLastUpdated.setFont(new Font("Dialog", Font.BOLD, 15));
		btnRefresh = new JButton("Refresh");
		_radMetric = new JRadioButton("Metric");
		_radMetric.setFont(new Font("Dialog", Font.BOLD, 15));
		_radImperial = new JRadioButton("Imperial");
		_radImperial.setFont(new Font("Dialog", Font.BOLD, 15));
        _radImperial.addActionListener(new ChangeUnitsAction(_radMetric, _radImperial, app));
        _radMetric.addActionListener(new ChangeUnitsAction(_radMetric, _radImperial, app));
		grpUnits = new ButtonGroup();
		grpUnits.add(_radMetric);
		grpUnits.add(_radImperial);

		//Action listener to handle when the refresh button is pressed
		btnRefresh.addActionListener(new RefreshAction(app));
				
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup( layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(_lblLastUpdated)
			) //end Group
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(btnRefresh)
			) //end Group
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(panelBlankSpace)
			) //end Group
			.addGroup(layout.createSequentialGroup()
				.addComponent(_radMetric)
				.addComponent(_radImperial)
			) //end Group (These are together)
		); //end HorizontalGroup (everything is in different columns)
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(_lblLastUpdated)	
				.addComponent(btnRefresh)
				.addComponent(panelBlankSpace)
				.addComponent(_radMetric)
				.addComponent(_radImperial)
			) //end  Group
		); //end VerticalGroup (everything is in the same row)

        displayUnits(app.getConfiguration().getLocaleSettings());
        displayLastUpdateTime(null); // default to 'never'

		this.setLayout(layout);
	} //end ChangeUnitsPanel

    /**
     * Displays the current {@link com.team23.weather.locale.LocaleSettings}.
     * @param localeSettings the current {@link com.team23.weather.locale.LocaleSettings}
     */
    private void displayUnits(LocaleSettings localeSettings) {
    	
        _radImperial.setSelected(false);
        _radMetric.setSelected(false);

        if( localeSettings.getName().equals("Metric") ) {
            _radMetric.setSelected(true);
        }
        
        if( localeSettings.getName().equals("Imperial") ) {
            _radImperial.setSelected(true);
        } //end if
        
    } //end displayUnits

    /**
     * Displays the last time the user queried the weather {@link com.team23.weather.models.Forecast}.
     * @param updated the {@link java.util.Date} of the last weather query
     */
    private void displayLastUpdateTime(Date updated) {
    	
        String text = updated == null ? "never" : Formatter.asTime(updated);
        _lblLastUpdated.setText("The weather was last updated at: " + text);
        
    } //end displayLastUpdateTime

    /**
     * Update the {@link javax.swing.JRadioButton}'s to reflect the {@link com.team23.weather.locale.LocaleSettings}.
     * @param localeSettings the current {@link com.team23.weather.locale.LocaleSettings}
     */
    @Override
    public void onLocaleChange(final LocaleSettings localeSettings) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayUnits(localeSettings);
            }
        });
    } //end onLocaleChange

    /**
     * Displays a message to show that the information is loading.
     */
    @Override
    public void beforeWeatherDataUpdate(WeatherData weatherData) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _lblLastUpdated.setText("The weather was last updated at: updating...");
            }
        });
    } //end beforeWeatherDataUpdate

    /**
     * Updates the time of the last query after the weather is updated.
     * @param weatherData the {@link com.team23.weather.models.WeatherData} with all the information
     */
    @Override
    public void onWeatherDataUpdated(final WeatherData weatherData) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayLastUpdateTime(weatherData.getLastUpdateTime());
            }
        });
    } //end onWeatherDataUpdated
    
} //end ChangeUnitsPanel
