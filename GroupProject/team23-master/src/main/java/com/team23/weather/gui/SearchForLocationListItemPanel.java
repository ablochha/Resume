package com.team23.weather.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * A SearchForLocationListItemPanel contains an entry that is displayed in the
 * {@link javax.swing.JList}. The entry contains the city name, current 
 * {@link com.team23.weather.models.Temperature}, and {@link com.team23.weather.models.SkyCondition}'s
 * image for a particular {@link com.team23.weather.models.Location}.
 * 
 * @author Saquib Mian
 * @see com.team23.weather.gui.SearchForLocationPanel
 * @see javax.swing.JList
 * @see com.team23.weather.models.Temperature
 * @see com.team23.weather.models.SkyCondition
 * @see com.team23.weather.models.Location
 */
public class SearchForLocationListItemPanel extends JPanel {

	/**
	 * _nameLabel contains the name of the city for the forecast
	 */
    private JLabel _nameLabel;
    
    /** 
     * _temperatureLabel contains the current temperature for the forecast
     */
    private JLabel _temperatureLabel;
    
    /**
     * _imageLabel contains the current sky condition for the forecast
     */
    private JLabel _imageLabel;

    /**
     * This constructs the entry in the {@link javax.swing.JList}.
     */
    public SearchForLocationListItemPanel() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        this.setLayout(layout);

        _imageLabel = new LazyImageLabel();
        _nameLabel = new JLabel();
        _temperatureLabel = new JLabel();


        this.add(_imageLabel);
        this.add(_nameLabel);
        this.add(_temperatureLabel);

        this.setEnabled(true);
        this.setOpaque(true);
    }

    /**
     * This sets the city name for the entry.
     * @param locationName the name of the city
     */
    public void setLocationName(String locationName) {
        _nameLabel.setText(locationName);
    }

    /**
     * This sets the {@link com.team23.weather.models.Temperature} for the entry.
     * @param temperature the {@link com.team23.weather.models.Temperature}
     */
    public void setTemperature(String temperature) {
        _temperatureLabel.setText(temperature);
    }

    /**
     * This sets the image of the {@link com.team23.weather.models.SkyCondition}.
     * @param url the url containing the icon
     */
    public void setIcon(String url) {
        try {
            Image image = ImageIO.read(new URL(url)).getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH);
            _imageLabel.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This changes the color of an entry's font depending on whether it has focus.
     * @param c the color to set the font
     */
    @Override
    public void setForeground(Color c) {
        if( _nameLabel != null ) {
            _nameLabel.setForeground(c);

        }
        if( _temperatureLabel != null ) {
            _temperatureLabel.setForeground(c);
        }
    }

}
