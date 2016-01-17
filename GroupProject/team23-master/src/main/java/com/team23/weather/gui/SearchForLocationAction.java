package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.models.Location;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A SearchForLocationAction implements {@link java.awt.event.ActionListener}.
 * 
 * A SearchForLocationAction causes {@link com.team23.weather.owm.OwmClient} to search for similar
 * cities. These similar cities are then displayed on {@link com.team23.weather.gui.SearchForLocationPanel}
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 *
 * @see com.team23.weather.App
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see com.team23.weather.gui.SearchForLocationActionContext
 * @see com.team23.weather.gui.SaveConfigurationAction
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 */
class SearchForLocationAction implements ActionListener {
	
	/**
	 * _app contains a reference to the backend of the program.
	 */
	private final App _app;
	
	/**
	 * _context contains a reference to the user's input.
	 */
	private final SearchForLocationActionContext _context;
	
	/**
	 * _panel contains a reference to the SearchForLocationPanel.
	 */
    private final SearchForLocationPanel _panel;

    /**
     * A SearchForLocationAction has a reference to {@link com.team23.weather.App}, {@link com.team23.weather.gui.SearchForLocationActionContext},
     * and {@link com.team23.weather.gui.SearchForLocationPanel}.
     * @param context the {@link com.team23.weather.gui.SearchForLocationActionContext} with the user's input
     * @param app a reference to {@link com.team23.weather.App}, the backend of the program
     * @param panel a reference to {@link com.team23.weather.gui.SearchForLocationPanel}, the user input panel
     */
	public SearchForLocationAction(SearchForLocationActionContext context, App app, SearchForLocationPanel panel) {
		
		_app = app;
		_context = context;

        _panel = panel;
        
    } //end SearchForLocationAction
	
	/**
	 * Causes {@link com.team23.weather.owm.OwmClient} to search for similar cities.
	 * @param e the {@link java.awt.event.ActionEvent} on the "search" button
	 */
	public void actionPerformed(ActionEvent e) {
		
		Thread worker = new Thread() {
			
			public void run() {

                String city = _context.getSearchForLocationCity().getText();
                String country = _context.getSearchForLocationCountry().getText();

                String query = city;
                if( country != null && !country.equals("")) query += "," + country;

				_context.getStatusField().setText("Searching...");
				Location[] locationResults = _app.searchForCity(query);
                _panel.setList(locationResults);
				_context.getStatusField().setText("");

			} //end run
			
		}; //end Thread
		
		worker.start();		
		
	} //end actionPerformed
	
} //end SearchForLocationAction