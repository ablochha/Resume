package com.team23.weather.gui;

import com.team23.weather.App;

import java.awt.event.KeyEvent;
import javax.swing.*;

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
 * @see javax.swing.JMenuItem
 *
 */
class ExitMenuItem extends JMenuItem {

    /**
	 * Displays the "Exit" toolbar, clicking this will exit the application.
	 * @param app a {@link com.team23.weather.App} containing a reference to the backend
	 */
    public ExitMenuItem(App app) {
    	
        super("Exit");

        this.setMnemonic(KeyEvent.VK_E);
        this.setToolTipText("Exit application");

        this.addActionListener(new ExitApplicationAction());
        
    } //end ExitMenuItem

}

