package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A LocationMenuItem extends {@link javax.swing.JMenuItem}
 * 
 * A LocationMenuItem is found under {@link com.team23.weather.gui.ViewMenu}.
 * Clicking on LocationMenuItem will open the {@link com.team23.weather.gui.SearchForLocationPanel}.
 * 
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.FileMenu
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 *
 */
class LocationMenuItem extends JMenuItem {

    /**
	 * Adds a {@link com.team23.weather.gui.LocationMenuItem} to the {@link com.team23.weather.gui.ViewMenu}.
	 * @param app {@link com.team23.weather.App} a reference to the backend of the program
	 */
    public LocationMenuItem(final App app) {
    	
        super("Location");

        this.setMnemonic(KeyEvent.VK_L);
        this.setToolTipText("View Location");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OpenWindowAction(new SearchForLocationPanelDialog(null, app)).actionPerformed(e);
            }
        });

    } //end ExitMenuItem

} //end SettingsMenuItem