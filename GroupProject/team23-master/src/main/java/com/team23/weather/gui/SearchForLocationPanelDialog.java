package com.team23.weather.gui;

import com.team23.weather.App;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * A SearchForLocationPanelDialog extends {@link javax.swing.JDialog}.
 * A SearchForLocationPanelDialog implements {#link java.awt.event.WindowListener}.
 * 
 * A SearchForLocationPanelDialog contains a reference to the {@link javax.swing.JFrame} 
 * for the {@link com.team23.weather.ScrollableGui} and forces focus on {@link com.team23.weather.gui.SearchForLocationPanel}.
 * A SearchForLocationPanelDialog also contains a reference to {@link com.team23.weather.App}
 * so that it can query the backend of the program.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see javax.swing.JFrame
 * @see javax.swing.JPanel
 * @see java.awt.Window
 * @see java.awt.event.WindowListener
 * 
 **/
public class SearchForLocationPanelDialog extends JDialog implements WindowListener {

	/**
	 * _parentFrame contains a reference to the frame of ScrollableGui.
	 */
    private final JFrame _parentFrame;
    
    /**
     * _app contains a reference to the backend of the program.
     */
    private final App _app;

    /**
     * Constructs the default size of the {@link javax.swing.JFrame}, adds the {@link javax.swing.JPanel}.
     * @param parentFrame the {@link javax.swing.JFrame} supporting the gui
     * @param app {@link com.team23.weather.App}, a reference to the backend of the program
     * @throws HeadlessException thrown if environment doesn't support keyboard, display, or mouse 
     */
    public SearchForLocationPanelDialog(JFrame parentFrame, App app) throws HeadlessException {
        super(parentFrame, "Search Locations", true);

        _parentFrame = parentFrame;
        _app = app;

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.add(new SearchForLocationPanel(app));

        Dimension size = new Dimension(500, 185);
        this.setPreferredSize(size);
        this.setMinimumSize(size);

        this.pack();
        this.addWindowListener(this);
    } //end SearchForLocationFrame

    @Override
    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    /**
     * When SearchForLocationPanelDialog is closed, {@link com.team23.weather.ScrollableGui} is opened.
     * @param e the {@link java.awt.event.WindowEvent}
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if(_app.getConfiguration().getCurrentLocation() == null) {
            System.exit(0);
        } else if(_parentFrame != null && !_parentFrame.isVisible()) {
            _parentFrame.setVisible(true);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
} //end SearchForLocationFrame