package com.team23.weather.gui;

import com.team23.weather.App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A RefreshAction implements a {@link java.awt.event.ActionListener}
 * to reference a user's action.
 * 
 * A RefreshAction will be triggered when a user clicks the 
 * {@link javax.swing.JButton} labelled "refresh" on the 
 * {@link com.team23.weather.gui.ChangeUnitsPanel}. A 
 * RefreshAction forces {@link com.team23.weather.gui.CurrentForecastPanel},
 * {@link com.team23.weather.gui.DailyForecastPanel}, and
 * {@link com.team23.weather.gui.HourlyForecastPanel} to update
 * their weather forecasts from {@link com.team23.weather.App}.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.ChangeUnitsPanel
 * @see com.team23.weather.gui.CurrentForecastPanel
 * @see com.team23.weather.gui.DailyForecastPanel
 * @see com.team23.weather.gui.HourlyForecastPanel
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 *
 */
class RefreshAction implements ActionListener {

	/**
	 * _app contains a reference to the backend of the program.
	 */
	private final App _app;

	/**
	 * The constructor stores a reference to app.
	 * @param app {@link com.team23.weather.App} is the backend of the program
	 */
    public RefreshAction(App app) {
    	
        _app = app;
        
	} //end RefreshAction
	
    /**
	 * Responds to a user performing an action related to the "refresh" button.
	 * @param event the user's action
	 */
	public void actionPerformed(ActionEvent event) {
		
		new Thread() {
			
			/**
			 * Runs a separate {@link java.lang.Thread} to {@link com.team23.weather.App} to refresh the weather.
			 */
            public void run(){
            	
                _app.refreshWeatherData(_app.getConfiguration().getCurrentLocation());
                
            } //end run
            
        }.start(); //end thread
        
	} //end actionPerformed
	
} //end RefreshAction