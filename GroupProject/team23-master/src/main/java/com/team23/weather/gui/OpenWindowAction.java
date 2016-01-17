package com.team23.weather.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An OpenWindowAction will make a new {@link java.awt.Window} visible for the user.
 * 
 * @author Saquib Mian
 * @see java.awt.Window
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.ActionListener
 *
 */
public class OpenWindowAction implements ActionListener {

	/**
	 * _window contains a reference to the {@link java.awt.Window} that will become visible.
	 */
    private final Window _window;

    /**
     * This constructor accepts a {@link java.awt.Window} to refer to.
     * @param window a {@link java.awt.Window} that will eventually be displayed
     */
    public OpenWindowAction(Window window) {
        _window = window;
    }

    /**
     * Displays the {@link java.awt.Window} to the user
     * @param event the {@link java.awt.event.ActionEvent} to open the {@link java.awt.Window}
     */
    public void actionPerformed(ActionEvent event) {
        _window.setVisible(true);
    }
	
}
