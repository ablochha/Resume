package com.team23.weather;

import com.team23.weather.errorhandling.Error;
import com.team23.weather.gui.*;
import com.team23.weather.gui.MenuBar;
import com.team23.weather.models.Location;
import com.team23.weather.errorhandling.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main GUI of the application
 *
 * @author Saquib Mian
 */
public class ScrollableGui extends JFrame implements IErrorHandler {

    /**
     * a reference to the backend
     */
    private final App _app;

    /**
     * the minimum width of the application window
     */
    private final static int DIMEN_WIDTH = 825;

    /**
     * the minimum height of the application window
     */
    private final static int DIMEN_HEIGHT = 740;

    /**
     * the size of the title bar on most systems
     */
    private final static int DIMEN_HEIGHT_TITLE_BAR = 44;

    /**
     * the minimum size of the application window
     */
    private final static Dimension WINDOW_SIZE = new Dimension(DIMEN_WIDTH, DIMEN_HEIGHT);

    /**
     * the pane that allows multiple panels to sit on top of each other; this is primarily useful for the error bar
     */
    private final JLayeredPane _layers;

    /**
     * the error bar
     */
    private final JPanel _errorPanel;

    /**
     * the main weather GUI
     */
    private final JComponent _mainPanel;

    /**
     * the error message displayed in the error bar
     */
    private JLabel _errorMessage = new JLabel();

    /**
     * the clear button in the error bar
     */
    private JButton _clearError = new JButton("clear");

    /**
     * Initializes the GUI, with menu bars and the appropriate panels.
     * @param app The backend
     */
    public ScrollableGui(App app) {
        super("Weather Application -- Team 23");

        _app = app;

        // log all errors to log4j
        _app.registerErrorHandler(new Log4JErrorLogger());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(WINDOW_SIZE);
        this.setMinimumSize(WINDOW_SIZE);
        this.setMaximumSize(WINDOW_SIZE);

        this.setJMenuBar(new MenuBar(_app));

        _layers = new JLayeredPane();
        this.setLayout(new BorderLayout());
        this.add(_layers, BorderLayout.CENTER);

        _errorPanel = createErrorPanel();
        _mainPanel = createMainPanel();
        _layers.add(_errorPanel, JLayeredPane.FRAME_CONTENT_LAYER);
        _layers.add(_mainPanel, JLayeredPane.DEFAULT_LAYER);

        this.pack();
    }

    /**
     * Creates the panel for the error bar, where errors are shown.
     * @return the completed error bar panel
     */
    private JPanel createErrorPanel(){
        final JPanel toReturn = new JPanel();

        _errorMessage.setFont(Fonts.Medium);

        final Dimension size = new Dimension((int) (DIMEN_WIDTH*.8), 35);
        toReturn.setBounds((DIMEN_WIDTH - size.width) / 2, DIMEN_HEIGHT - DIMEN_HEIGHT_TITLE_BAR - size.height * 2, size.width, size.height);
        toReturn.setMaximumSize(size);
        toReturn.setOpaque(true);
        toReturn.setBackground(Color.RED);
        toReturn.add(_errorMessage);
        toReturn.add(_clearError);
        toReturn.setBorder(BorderFactory.createLineBorder(Color.black));

        _app.registerErrorHandler(this);
        _clearError.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _errorMessage.setText("");
                _layers.setLayer(toReturn, JLayeredPane.FRAME_CONTENT_LAYER);
            }
        });

        return toReturn;
    }

    /**
     * Creates the main panel with the weather UI components.
     * @return the completed panel
     */
    private JComponent createMainPanel() {
        JPanel toReturn = new JPanel();

        // DIMEN_HEIGHT_TITLE_BAR is the height of the title bar, because the setBounds API
        // interprets the rectangle different from window size
        toReturn.setBounds(0,0, DIMEN_WIDTH, DIMEN_HEIGHT - DIMEN_HEIGHT_TITLE_BAR);

        // Initialize all of the panels
        final float alignment = Component.CENTER_ALIGNMENT;
        JPanel cup = new ChangeUnitsPanel(_app);
        JPanel locationPanel = new LocationPanel(_app);
        JComponentLabel currentForecastPanel = new JComponentLabel(
                "Current Weather",
                new CurrentForecastPanel(_app),
                alignment,
                Fonts.Large
        );
        JComponentLabel dailyForecastPanel = new JComponentLabel(
                "Next few days",
                new DailyForecastPanel(_app),
                alignment,
                Fonts.Large
        );
        JComponentLabel hourlyForecastPanel = new JComponentLabel(
                "Next 24 hours",
                wrapInScrollPane(new HourlyForecastPanel(_app)),
                alignment,
                Fonts.Large
        );

        locationPanel.setBackground(Colors.HeaderColor);
        // TODO heights of these are different
        currentForecastPanel.setLabelBackground(Colors.ComponentHeaderColor);
        dailyForecastPanel.setLabelBackground(Colors.ComponentHeaderColor);
        hourlyForecastPanel.setLabelBackground(Colors.ComponentHeaderColor);

        // Lay out the panels
        GroupLayout layout = new GroupLayout(toReturn);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(locationPanel)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addComponent(currentForecastPanel)
                                        .addComponent(dailyForecastPanel)
                        )
                        .addComponent(hourlyForecastPanel)
                        .addComponent(cup)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(locationPanel)
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(currentForecastPanel)
                                        .addComponent(dailyForecastPanel)
                        )
                        .addComponent(hourlyForecastPanel)
                        .addComponent(cup)
        );

        toReturn.setLayout(layout);

        return toReturn;
    }

    /**
     * Helper method to wrap any {@link javax.swing.JPanel} in a {@link javax.swing.JScrollPane}
     * @param panel the panel to wrap
     * @return the wrapped panel
     */
    private JScrollPane wrapInScrollPane(JPanel panel) {
        JScrollPane toReturn = new JScrollPane(panel);

        toReturn.setBorder(BorderFactory.createEmptyBorder());
        toReturn.setPreferredSize(WINDOW_SIZE);
        toReturn.getVerticalScrollBar().setUnitIncrement(16);
        toReturn.getHorizontalScrollBar().setUnitIncrement(16);

        return toReturn;
    }

    /**
     * This method overrides {@link javax.swing.JFrame}'s to control
     * the application OOBE (Out Of Box Experience) (i.e. the first start).
     * It is intended to be called by {@link com.team23.weather.GuiRunner}.
     * @param visible should this frame be visible.
     */
    @Override
    public void setVisible(boolean visible) {
        // If we don't have a location as yet, show the search for location panel
        Location location = _app.getConfiguration().getCurrentLocation();
        if( location == null || location.getLocationId() <= 0 ) {
            new SearchForLocationPanelDialog(this, _app).setVisible(true);
        } else {
            super.setVisible(visible);
        }

    } //end setVisible

    /**
     * This method completes the {@link IErrorHandler} interface and is executed when an error occurs.
     *
     * This particular implementation displays the error bar with the appropriate message.
     * @param e the error
     */
    @Override
    public void handleError(Error e) {
        _errorMessage.setText(e.getMessage());
        _layers.setLayer(_errorPanel, JLayeredPane.MODAL_LAYER);
    }
}
