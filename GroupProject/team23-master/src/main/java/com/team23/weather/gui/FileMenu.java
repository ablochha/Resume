package com.team23.weather.gui;

import com.team23.weather.App;

import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * A FileMenu is a {@link javax.swing.JMenu} that is found
 * along the top window toolbar. FileMenu contains a
 * {@link com.team23.weather.gui.ExitMenuItem}.
 *
 * @author Andrew Bloch-Hansen
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.ExitMenuItem
 * @see com.team23.weather.gui.ViewMenu
 * @see com.team23.weather.gui.ExitApplicationAction
 * @see javax.swing.JMenu
 *
 */
class FileMenu extends JMenu {

    /**
	 * Contains a list of options to appear under the File toolbar.
	 * @param app a {@link com.team23.weather.App} containing a reference to the backend
	 */
    public FileMenu(App app) {
    	
        super("File");

        this.setMnemonic(KeyEvent.VK_F);
		this.add(new ExitMenuItem(app));

	} //end ExitMenuItem
	
} //end FileMenu