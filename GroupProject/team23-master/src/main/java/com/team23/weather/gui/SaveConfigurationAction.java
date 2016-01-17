package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A SaveConfigurationAction implements a {@link java.awt.event.ActionListener}
 * to reference a user's action.
 * 
 * A SaveConfigurationAction will be triggered when a user clicks the 
 * {@link javax.swing.JButton} labelled "Save Location" on the 
 * {@link com.team23.weather.gui.SearchForLocationPanel}. A SaveConfigurationAction
 * will write the contents of a {@link com.team23.weather.gui.SearchForLocationActionContext}
 * to a {@link com.team23.weather.models.GeoCoordinate}, {@link com.team23.weather.models.Location}, 
 * {@link com.team23.weather.configuration.Configuration}, {@link com.team23.weather.locale.LocaleSettings}
 * stored in a {@link com.team23.weather.App}.
 * A SaveConfigurationAction will also save this data to a file called "config.json".
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui 
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.LocationMenuItem
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see com.team23.weather.models.GeoCoordinate
 * @see com.team23.weather.models.Location
 * @see com.team23.weather.configuration.Configuration
 * @see com.team23.weather.locale.LocaleSettings
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 *
 */
class SaveConfigurationAction implements ActionListener {

	/**
	 * _context refers to an object containing the user's input on the SearchForLocationPanel
	 */
	private final SearchForLocationActionContext _context;
	
	/**
	 * _app contains a reference to the backend of the program.
	 */
    private final App _app;
	
    /**
	 * The constructor stores a reference to the {@link com.team23.weather.gui.SearchForLocationActionContext}
	 * and to {@link com.team23.weather.App}.
	 * @param context the {@link com.team23.weather.gui.SearchForLocationActionContext} referencing the user's input
	 * @param app {@link com.team23.weather.App} is the backend of the program
	 */
	public SaveConfigurationAction(SearchForLocationActionContext context, App app) {
		
		_app = app;
		_context = context;        
        
    } //end SaveConfigurationAction
	
	/**
	 * Responds to a user performing an action related to the "Save Location" button on the 
	 * {@link com.team23.weather.gui.SearchForLocationPanel}.
	 * @param event the user's action
	 */
	public void actionPerformed(ActionEvent event) {
        ((JComponent)event.getSource()).setEnabled(false);

        // save configuration
        Thread worker = new Thread() {
        	
        	/**
			 * Runs a separate {@link java.lang.Thread} to {@link com.team23.weather.App} to save a
			 * {@link com.team23.weather.configuration.Configuration} to "config.json".
			 */
            public void run() {
                _app.saveLocation(_context.getSelectedLocation());
                _context.getStatusField().setText("Location successfully saved! Please close this window.");
            } //end run
            
        }; //end Thread
        
        worker.start();

	} //end actionPerformed
	
} //end SaveConfigurationAction