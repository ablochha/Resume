package com.team23.weather.gui;

import com.team23.weather.models.Location;

import javax.swing.*;

/**
 * A SearchForLocationActionContext is an object containing all the user's input from the
 * {@link com.team23.weather.gui.SearchForLocationPanel}.
 * 
 * @author Andrew Bloch-Hansen
 *
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see com.team23.weather.gui.SearchForLocationAction
 * @see com.team23.weather.gui.SaveConfigurationAction
 * @see com.team23.weather.models.Location
 * @see javax.swing.JList
 * @see javax.swing.JTextField
 */
class SearchForLocationActionContext {

	/**
	 * _searchForLocationList contains the list of suggested cities.
	 */
    private JList _searchForLocationList;
    
    /**
     * _txtSearchForLocationCity contains the users input for city.
     */
	private JTextField _txtSearchForLocationCity;
	
	/**
	 * _txtSearchForLocationCountry contains the users input for country.
	 */
	private JTextField _txtSearchForLocationCountry;

	private JLabel _statusField;

	/**
	 * This stores a reference to the list of suggested cities.
	 * @param searchForLocationList the {@link javax.swing.JList} containing the list of suggested cities
	 */
	public SearchForLocationActionContext(JList searchForLocationList) {
		
        _searchForLocationList = searchForLocationList;
        
    } //end SearchForLocationActionContext

	/**
	 * Returns the list of suggested cities.
	 * @return the {@link javax.swing.JList} containing the list of suggested cities
	 */
	public JList getSearchForLocationList() {
		
		return _searchForLocationList;
		
	} //end getSearchForLocationList
	
	/**
	 * Sets the list of suggested cities.
	 * @param searchForLocationList the {@link javax.swing.JList} containing the list of suggested cities
	 */
	public void setSearchForLocationList(JList searchForLocationList) {
		
		_searchForLocationList = searchForLocationList;
		
	} //end setSearchForLocationList() 
	
	/**
	 * Returns the {@link com.team23.weather.models.Location} of the selected item.
	 * @return the {@link com.team23.weather.models.Location} of the selected item
	 */
	public Location getSelectedLocation() {
		
		return (Location)_searchForLocationList.getSelectedValue();
		
	} //end getSelectedLocation

	/**
	 * Returns the {@link javax.swing.JTextField} for the user's input for "City".
	 * @return a {@link javax.swing.JTextField} for "City"
	 */
	public JTextField getSearchForLocationCity() {

		return _txtSearchForLocationCity;

	} //end getSearchForLocationCity

	/**
	 * Sets the {@link javax.swing.JTextField} for the user's input for "City".
	 * @param txtSearchForLocationCity {@link javax.swing.JTextField} for "City"
	 */
	public void setSearchForLocationCity(JTextField txtSearchForLocationCity) {

		_txtSearchForLocationCity = txtSearchForLocationCity;

	} //end setSearchForLocationCity

	/**
	 * Returns the {@link javax.swing.JTextField} for the user's input for "Country".
	 * @return a {@link javax.swing.JTextField} for "Country"
	 */
	public JTextField getSearchForLocationCountry() {

		return _txtSearchForLocationCountry;

	} //end getSearchForLocationCity

	/**
	 * Sets the {@link javax.swing.JTextField} for the user's input for "Country".
	 * @param txtSearchForLocationCountry {@link javax.swing.JTextField} for "Country"
	 */
	public void setSearchForLocationCountry(JTextField txtSearchForLocationCountry) {

		_txtSearchForLocationCountry = txtSearchForLocationCountry;

	} //end setSearchForLocationCity

	/**
	 * Sets the {@link JLabel} indicating the status
	 * @param statusField the {@link JLabel} indicating the status
	 */
	public void setStatusField(JLabel statusField) {
		_statusField = statusField;
	}

	/**
	 * Gets the {@link JLabel} indicating the status
	 * @return A {@link JLabel} indicating the status
	 */
	public JLabel getStatusField() {
		return _statusField;
	}

} //end SearchForLocationActionContext