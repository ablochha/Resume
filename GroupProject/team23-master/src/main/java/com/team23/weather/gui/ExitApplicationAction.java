package com.team23.weather.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An ExitMenuItem is a {@link javax.swing.JMenuItem} that is found
 * under the File {@link javax.swing.JMenu}. Clicking on ExitMenuItem
 * will call {@link com.team23.weather.gui.ExitApplicationAction} and 
 * quit the program.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.FileMenu
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.ExitApplicationAction
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 *
 */
class ExitApplicationAction implements ActionListener{

	/**
	 * Responds to a user performing an action related to its object
	 * @param event the user's action
	 */
	public void actionPerformed(ActionEvent event) {
		
		System.exit(0);
		
	} //end actionPerformed
	
} //end ExitApplicationAction