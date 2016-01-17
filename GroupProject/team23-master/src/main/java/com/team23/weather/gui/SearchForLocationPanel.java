package com.team23.weather.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.team23.weather.App;
import com.team23.weather.configuration.Configuration;
import com.team23.weather.locale.LocaleSettings;
import com.team23.weather.models.Location;

/**
 * A SearchForLocationPanel extends {@link javax.swing.JPanel}.
 * A SearchForLocationPanel implements the {@link javax.swing.ListCellRenderer}
 * interface, to help build the {@link javax.swing.JList}
 * A SearchForLocationPanel implements {@link javax.swing.event.ListSelectionListener}.
 * 
 * A SearchForLocationPanel allows the user to set the Location for the {@link com.team23.weather.owm.OwmClient}.
 * Changing location will update the {@link com.team23.weather.ScrollableGui} with the weather.
 * Saving a location will cause the {@link com.team23.weather.configuration.ConfigurationManager} to write a
 * {@link com.team23.weather.configuration.Configuration} to file.
 * {@link com.team23.weather.App} manages all of these processes through the SearchForLocationPanel.
 * 
 * @author Andrew Bloch-Hansen
 * @author Saquib Mian
 * @see com.team23.weather.App
 * @see com.team23.weather.ScrollableGui
 * @see com.team23.weather.owm.OwmClient
 * @see com.team23.weather.gui.SearchForLocationAction
 * @see com.team23.weather.gui.SearchForLocationActionContext
 * @see com.team23.weather.gui.SaveConfigurationAction
 * @see javax.swing.JPanel
 * @see javax.swing.JList
 * @see javax.swing.ListCellRenderer
 * @see javax.swing.event.ListSelectionListener 
 *
 */
class SearchForLocationPanel extends JPanel implements ListCellRenderer<Location>, ListSelectionListener {
	
	/**
	 * _app contains a reference to the backend of the program.
	 */
	private final App _app;
	
	/**
	 * _txtCity contains a reference to the user's input for "City".
	 */
    private final JTextField _txtCity;
    
    /**
	 * _txtCountry contains a reference to the user's input for "Country".
	 */
    private final JTextField _txtCountry;
    
    /**
	 * _searchForLocationList contains a reference to the output of suggested locations.
	 */
    private final JList<Location> _searchForLocationList;

    /**
	 * _btnSearch contains a reference to the "Search" button.
	 */
    private final JButton _btnSearch;
    
    /**
	 * _btnSaveLocation contains a reference to the "Save Location" button.
	 */
    private final JButton _btnSaveLocation;

    /**
     * _txtStatus contains the status of any operations conducted using this panel
     */
    private final JLabel _txtStatus;
    
    /**
     * Constructs a {@link javax.swing.JPanel} to display location change components.
     * @param app {@link com.team23.weather.App} a reference to the backend of the program
     */
    public SearchForLocationPanel(App app) {

		_app = app;

		_txtCity = new JTextField();
		_txtCountry = new JTextField();
        _txtStatus = new JLabel(" ");

		_txtCity.setMaximumSize(new Dimension(125,23));
		_txtCountry.setMaximumSize(new Dimension(125,23));

        _searchForLocationList = new JList<Location>();
        _searchForLocationList.setCellRenderer(this);
        _searchForLocationList.setFont(Fonts.Medium);
        _searchForLocationList.addListSelectionListener(this);

        _searchForLocationList.setBackground(Colors.ListItemBackgroundColor);
        _searchForLocationList.setForeground(Colors.ListItemForegroundColor);
        _searchForLocationList.setSelectionBackground(Colors.SelectedListItemBackgroundColor);
        _searchForLocationList.setSelectionForeground(Colors.SelectedListItemForegroundColor);

        SearchForLocationActionContext context = new SearchForLocationActionContext(_searchForLocationList);
        context.setSearchForLocationCity(_txtCity);
        context.setSearchForLocationCountry(_txtCountry);
        context.setStatusField(_txtStatus);

        _btnSearch = new JButton("Search");
        _btnSaveLocation = new JButton("Save Location");
        _btnSaveLocation.setEnabled(false);

        _btnSaveLocation.addActionListener(new SaveConfigurationAction(context, _app));
        _btnSearch.addActionListener(new SearchForLocationAction(context, _app, this));

        JPanel searchPanel = createSearchPanel();
        JPanel listPanel = createListPanel();

        GroupLayout l = new GroupLayout(this);
        l.setAutoCreateGaps(true);
        l.setVerticalGroup(
                l.createSequentialGroup()
                        .addGroup(
                                l.createParallelGroup()
                                        .addComponent(searchPanel)
                                        .addComponent(listPanel)
                        )
                        .addComponent(_txtStatus)
        );
        l.setHorizontalGroup(
                l.createParallelGroup()
                        .addGroup(
                                l.createSequentialGroup()
                                        .addComponent(searchPanel)
                                        .addComponent(listPanel)
                        )
                        .addComponent(_txtStatus)
        );

        this.setLayout(l);

        loadConfiguration();

    } //end SearchForLocationPanel

    /**
     * This {@link javax.swing.JPanel} contains the user input components. 
     * @return a {@link javax.swing.JPanel} with user input components
     */
    private JPanel createSearchPanel() {
    	
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        searchPanel.add(new JComponentLabel("City", _txtCity, Component.LEFT_ALIGNMENT));
        searchPanel.add(new JComponentLabel("Country", _txtCountry, Component.LEFT_ALIGNMENT));
        searchPanel.add(_btnSearch);
        searchPanel.add(_btnSaveLocation);
        return searchPanel;
        
    } //end createSearchPanel

    /**
     * This {@link javax.swing.JPanel} contains the list of suggested cities. 
     * @return a {@link javax.swing.JPanel} with the list of suggested cities
     */
    private JPanel createListPanel() {
    	
        JScrollPane scrollableList = wrapInScrollPane(_searchForLocationList);
        scrollableList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        return new JComponentLabel("Please select one", scrollableList, Component.LEFT_ALIGNMENT);
        
    } //end createListPanel

    /**
     * This {@link javax.swing.JScrollPane} adds a scrollbar to the list of suggested cities.
     * @param panel the {@link javax.swing.JComponent} to add a {@link javax.swing.JScrollPane} to
     * @return a {@link javax.swing.JScrollPane} containing the input {@link javax.swing.JComponent}
     */
    private JScrollPane wrapInScrollPane(JComponent panel) {
    	
        JScrollPane toReturn = new JScrollPane(panel);

        toReturn.setBorder(BorderFactory.createEmptyBorder());
        toReturn.setPreferredSize(new Dimension(500,100));
        toReturn.getVerticalScrollBar().setUnitIncrement(16);
        toReturn.getHorizontalScrollBar().setUnitIncrement(16);

        return toReturn;
        
    } //end wrapInScrollPane
	
    /**
     * Automatically display the default configuration on the {@link com.team23.weather.gui.SearchForLocationPanel}.
     */
	private void loadConfiguration() {
        new SwingWorker<Configuration, Void>() {
            private Configuration _configuration;

            /**
             * Delegate {@link com.team23.weather.App} to load the {@link com.team23.weather.configuration.Configuration}.
             * @return a {@link com.team23.weather.configuration.Configuration} from {@link com.team23.weather.App}
             */
            @Override
            protected Configuration doInBackground() throws Exception {
                return _app.getConfiguration();
            }

            /**
             * Display the configuration on the {@link com.team23.weather.gui.SearchForLocationPanel}.
             */
            @Override
            protected void done() {
                try {
                    _configuration = get();
                    displayConfiguration(_configuration);
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        }.execute();
    }

	/**
	 * Display the configuration on the {@link com.team23.weather.gui.SearchForLocationPanel}.
	 * @param configuration the {@link com.team23.weather.configuration.Configuration} loaded from {@link com.team23.weather.App}
	 */
    private void displayConfiguration(Configuration configuration) {
    	
        Location location = configuration.getCurrentLocation();

        if( location == null ) {
        	
            return;
            
        } //end if
   
        _txtCity.setText(location.getCity());
        _txtCountry.setText(location.getCountry());

    } //end displayConfiguration

    /**
     * Sets the contents of the {@link javax.swing.JList} with the list of suggested cities
     * @param list an array of {@link com.team23.weather.models.Location}
     */
    void setList(Location[] list) {
    	
        _searchForLocationList.setListData(list);
        _btnSaveLocation.setEnabled(false);

        
    } //end setList

    /**
     * This panel is rendered for each item in the JList above
     */
    private SearchForLocationListItemPanel _renderedListPanel = new SearchForLocationListItemPanel();

    /**
     * Returns the value of an item in the {@link javax.swing.JList}
     * @param list the list of suggested cities
     * @param value a particular city from the list
     * @param index the index in the list for the city
     * @param isSelected the boolean value if a city is selected
     * @param cellHasFocus the boolean value if a city has focus
     * @return the value of the selected item in the {@link javax.swing.JList}
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Location> list, Location value, int index, boolean isSelected, boolean cellHasFocus) {
        LocaleSettings localeSettings = _app.getConfiguration().getLocaleSettings();
        String imageUrl = value.getWeather().getSkyCondition().getIconUrl();

        _renderedListPanel.setLocationName(value.getCity() + ", " + value.getCountry() + ", ");
        _renderedListPanel.setTemperature(value.getWeather().getTemperature().getCurrent().toString(localeSettings));
        _renderedListPanel.setIcon(imageUrl);
        _renderedListPanel.setFont(list.getFont());

        if (isSelected) {
            _renderedListPanel.setBackground(list.getSelectionBackground());
            _renderedListPanel.setForeground(list.getSelectionForeground());
        } else {
            _renderedListPanel.setBackground(list.getBackground());
            _renderedListPanel.setForeground(list.getForeground());
        }

        return _renderedListPanel;
        
    } //end getListCellRendererComponent

    /**
     * Allows the user to use the {@link javax.swing.JButton}'s if they have selected an item from the {@link javax.swing.JList}.
     * @param e the action event for selected an item
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
    	
        _btnSaveLocation.setEnabled(true);

        
    } //end valueChanged
    
} // end SearchForLocationPanel