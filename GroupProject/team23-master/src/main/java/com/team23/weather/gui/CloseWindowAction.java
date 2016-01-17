package com.team23.weather.gui;

import java.awt.*;
import java.awt.event.*;

/**
 * A CloseWindowAction will remove a {@link java.awt.Window} from memory and
 * make it invisible.
 * 
 * @author Saquib Mian
 * @see java.awt.Window
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 *
 */
class CloseWindowAction implements ActionListener {

	/**
	 * _window contains a reference to a JFrame it will close.
	 */
    private final Window _window;

    /**
     * Stores a reference to a {@link java.awt.Window} that it will close
     * @param window a reference to a {@link java.awt.Window}
     */
    public CloseWindowAction(Window window) {

        _window = window;
        
    } //end CloseWindowAction

    /**
	 * Responds to a user performing an action related to its object
	 * @param event the user's action
	 */
    public void actionPerformed(ActionEvent event) {

        _window.dispose();

    } //end actionPerformed

} //end CloseWindowAction