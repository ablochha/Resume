package com.team23.weather.gui;

import com.team23.weather.App;
import com.team23.weather.locale.LocaleSettings;

import javax.swing.*;
import java.awt.event.*;

/**
 * A ChangeUnitsAction implements a {@link java.awt.event.ActionListener}
 * to reference a user's action.
 * 
 * A ChangeUnitsAction will be triggered when a user clicks the 
 * {@link javax.swing.JRadioButton} labelled "Metric" or "Imperial" on the 
 * {@link com.team23.weather.gui.ChangeUnitsPanel}. A ChangeUnitsAction
 * will update the {@link com.team23.weather.locale.LocaleSettings} for {@link com.team23.weather.App}.
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * 
 * @see com.team23.weather.App 
 * @see com.team23.weather.gui.ChangeUnitsPanel
 * @see com.team23.weather.locale.LocaleSettings
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 * @see javax.swing.JRadioButton
 *
 */
class ChangeUnitsAction implements ActionListener {

	/**
	 * _app contains a reference to the backend of the program.
	 */
	private final App _app;
	
	/**
	 * _radMetric contains a reference to the button "Metric".
	 */
	private final JRadioButton _radMetric;
	
	/**
	 * _radImperial contains a reference to the button "Metric".
	 */
	private final JRadioButton _radImperial;    

	/**
	 * _newLocaleSettings contains a reference to the settings indicated by the radio buttons.
	 */
    private LocaleSettings _newLocaleSettings;

    /**
     * Sets references to {@link com.team23.weather.App} and two {@link javax.swing.JRadioButton}'s.
     * @param radMetric the {@link javax.swing.JRadioButton} for "Metric"
     * @param radImperial the {@link javax.swing.JRadioButton} for "Imperial"
     * @param app {@link com.team23.weather.App}, a reference to the back end of the program
     */
    public ChangeUnitsAction(JRadioButton radMetric, JRadioButton radImperial, App app) {
		
    	_app = app;
    	_radMetric = radMetric;		
		_radImperial = radImperial;     
        
    } //end ChangeUnitsAction
	
    /**
	 * Responds to a user performing an action related to the "Metric" and "Imperial" radio buttons on the 
	 * {@link com.team23.weather.gui.ChangeUnitsPanel}.
	 * @param event the user's action
	 */
	public void actionPerformed(ActionEvent event) {
		
        if (!_radMetric.isSelected() && !_radImperial.isSelected()) {
            System.out.println("Please select a unit");
            return;
        } //end if

		if (_radMetric.isSelected()) {
            _newLocaleSettings = LocaleSettings.getMetricSettings();
        } //end if 
		
		else if (_radImperial.isSelected()) {
            _newLocaleSettings = LocaleSettings.getImperialSettings();
        } //end else if

        // save configuration
        Thread worker = new Thread() {
            public void run() {
                _app.setLocaleSettings(_newLocaleSettings);
            } //end run
        }; //end Thread
        
        worker.start();
        
	} //end actionPerformed
	
} //end ChangeUnitsAction
