package com.team23.weather.models;

import java.awt.*;

/**
 * This model represents the condition of the sky
 *
 * @author Saquib Mian
 */
public class SkyCondition {
    /**
     * The clear name of the condition, like 'Cloudy'
     */
    private String _title;

    /**
     * The description of the condition, like 'cloudy with some sun'
     */
    private String _description;

    /**
     * The name of the image file representing the sky condition
     */
    private String _iconFileName;

    private String _iconUrl;

    /**
     * The image representing the sky condition
     */
    private Image _image;

    /**
     *
     * @param title The clear name of the condition, like 'Cloudy'
     * @param description The description of the condition, like 'cloudy with some sun'
     * @param iconFileName The name of the image file representing the sky condition
     */
    public SkyCondition(String title, String description, String iconFileName) {
        _title = title;
        _description = description;
        _iconFileName = iconFileName;
    }

    /**
     * get the title
     * @return the title
     */
    public String getTitle() {
        return _title;
    }

    /**
     * set the title
     * @param title the new title
     */
    public void setTitle(String title) {
        _title = title;
    }

    /**
     * get the description
     * @return the description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * set the description
     * @param description the description
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * get the icon file name
     * @return the icon file name
     */
    public String getIconFileName() {
        return _iconFileName;
    }

    /**
     * get the icon file name
     * @param iconFileName the icon file name
     */
    public void setIconFileName(String iconFileName) {
        _iconFileName = iconFileName;
    }

    /**
     * get the icon image
     * @return the icon image
     */
    public Image getImage() {
        return _image;
    }

    /**
     * set the icon image
     * @param image the icon image
     */
    public void setImage(Image image) {
        _image = image;
    }

    public String getIconUrl() {
        return _iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        _iconUrl = iconUrl;
    }
}
