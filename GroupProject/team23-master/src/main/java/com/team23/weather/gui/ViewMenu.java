package com.team23.weather.gui;

import com.team23.weather.App;

import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * A ViewMenu extends {@link javax.swing.JMenu}.
 * 
 * A ViewMenu is found along the top window toolbar. 
 * ViewMenu contains a {@link com.team23.weather.gui.LocationMenuItem}.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.ExitMenuItem
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.LocationMenuItem
 * @see com.team23.weather.gui.ExitApplicationAction
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuBar
 *
 */
class ViewMenu extends JMenu {

    /**
	 * Adds options that appear under the View toolbar.
	 * @param app {@link com.team23.weather.App} a reference to the backend of the program
	 */
	public ViewMenu(App app) {
		
        super("View");

		this.setMnemonic(KeyEvent.VK_V);
		
		this.add(new LocationMenuItem(app));
        this.add(new ViewMarsWeatherMenuItem(app));
        
	} //end ExitMenuItem
		
} //end FileMenu