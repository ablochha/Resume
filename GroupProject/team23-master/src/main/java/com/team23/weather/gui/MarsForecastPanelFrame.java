package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link javax.swing.JFrame} wraps the {@link com.team23.weather.gui.MarsForecastPanel}, so that it can be displayed as a separate window.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.FileMenu
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.ViewMarsWeatherMenuItem
 * @see com.team23.weather.gui.MarsForecastPanel
 * @see javax.swing.JFrame
 * @see javax.swing.JPanel
 */
class MarsForecastPanelFrame extends JFrame {

	/**
	 * Passes {@link com.team23.weather.App} through to a new frame, and launches {@link com.team23.weather.gui.MarsForecastPanel}.
	 * @param app {@link com.team23.weather.App} a reference to the backend of the program
	 */
    public MarsForecastPanelFrame(App app) throws HeadlessException {
        super("Mars Forecast");
    	
        this.setPreferredSize(new Dimension());
        this.add(new MarsForecastPanel(app));

        Dimension size = new Dimension(250, 320);
        this.setPreferredSize(size);
        this.pack();
        
    } //end MarsForecastPanelFrame
    
} //end MarsForecastPanelFrame