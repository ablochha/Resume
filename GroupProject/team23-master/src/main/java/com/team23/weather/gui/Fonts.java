package com.team23.weather.gui;

import java.awt.*;

/**
 * The Fonts are a collection of predefined Font Styles
 * and sizes. The Fonts come in Small, Medium, and Large.
 * The Font Style is Dialog.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.gui.LocationPanel
 * @see com.team23.weather.gui.CurrentForecastPanel
 * @see com.team23.weather.gui.DailyForecastPanel
 * @see com.team23.weather.gui.HourlyForecastPanel
 * @see com.team23.weather.gui.MarsForecastPanel
 * @see com.team23.weather.gui.ChangeUnitsPanel
 * @see java.awt.Font
 *
 */
public class Fonts {
	
	/**
	 * Large contains the Font Style "Dialog" set to BOLD in size 25.
	 */
    public static final Font Large = new Font("Dialog", Font.BOLD, 25);
    
    /**
	 * Medium contains the Font Style "Dialog" set to BOLD in size 15.
	 */
    public static final Font Medium = new Font("Dialog", Font.BOLD, 15);
    
    /**
	 * Small contains the Font Style "Dialog" set to BOLD in size 10.
	 */
    public static final Font Small = new Font("Dialog", Font.BOLD, 10);
    
} //end Fonts