package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * A ViewMarsWeatherMenuItem extends {@link javax.swing.JMenuItem}.
 * 
 * A ViewMarsWeatherMenuItem is found under {@link com.team23.weather.gui.ViewMenu}. 
 * Clicking on a ViewMarsWeatherMenuItem will open the {@link com.team23.weather.gui.MarsForecastPanel}.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.FileMenu
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.MarsForecastPanel
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 *
 */
class ViewMarsWeatherMenuItem extends JMenuItem {

	/**
	 * Adds a {@link com.team23.weather.gui.ViewMarsWeatherMenuItem} to the {@link com.team23.weather.gui.ViewMenu}.
	 * @param app {@link com.team23.weather.App} a reference to the backend of the program
	 */
    public ViewMarsWeatherMenuItem(App app) {
        super("Mars Weather");

        this.setMnemonic(KeyEvent.VK_M);
        this.setToolTipText("Mars Weather");

        this.addActionListener(new OpenWindowAction(new MarsForecastPanelFrame(app)));
    } //end ViewMarsWeatherMenuItem

} //end ViewMarsWeatherMenuItem