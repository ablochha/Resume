package com.team23.weather;

/**
 * This class decides which GUI to run. It should be provided as
 * an argument to <code>SwingUtilities.InvokeLater()</code> so
 * that it can be run on the EDT.
 *
 * @author Saquib Mian
 */
class GuiRunner implements Runnable {

    /**
     * the backend
     */
    private final App _app;
	
	public GuiRunner( App app ) {
		_app = app;
	}

    /**
     * this method begins the GUI logic on the EDT
     */
    public void run() {
		new ScrollableGui(_app).setVisible( true );
	}
	
}
