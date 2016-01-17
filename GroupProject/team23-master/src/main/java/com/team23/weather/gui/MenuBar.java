package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;

/**
 * The menu bar for the application.
 *
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * 
 * @see com.team23.weather.gui.FileMenu
 * @see com.team23.weather.gui.ViewMenu
 * @see javax.swing.JMenuBar
 */
public class MenuBar extends JMenuBar {

	/**
	 * Creates a new menu bar with a {@link com.team23.weather.gui.FileMenu} and a {@link com.team23.weather.gui.ViewMenu}
	 * @param app {@link com.team23.weather.App} a reference to the backend of the program
	 */
    public MenuBar(App app) {
    	
        this.add(new FileMenu(app));
        this.add(new ViewMenu(app));
        
    } //end MenuBar
    
} //end MenuBar