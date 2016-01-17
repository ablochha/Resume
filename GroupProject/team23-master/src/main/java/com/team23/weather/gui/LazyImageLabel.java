package com.team23.weather.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Swing component extends a {@link JLabel} to allow for lazily loading icons.
 *
 * By default, an icon must be fully loaded into memory before it can be used in a
 * Swing component. This component extends that functionality to allow for loading
 * icons in a background thread and displaying an intermediary loading icon in the
 * meantime.
 *
 * Note: Do not use this component in another component that is immediately rendered,
 * unless you make sure to re-draw the component after you are sure the icon has
 * been loaded.
 *
 * @author Saquib Mian
 */
public class LazyImageLabel extends JLabel implements Runnable {

    /**
     * the log4j logger
     */
    static Logger logger = LogManager.getLogger(LazyImageLabel.class.getName());

    /**
     * thread-safe icon cache
     */
    private static final ConcurrentHashMap<String,Image> s_cachedIcons = new ConcurrentHashMap<String, Image>();

    /**
     * the key in the icon cache for the loading placeholder icon
     */
    private static String s_loadingGifKey = "loading";

    /**
     * default width for icons (50px)
     */
    private int _iconWidth = 50;

    /**
     * default height for icons (50px)
     */
    private int _iconHeight = 50;

    /**
     * the remote url to be loaded and set as the icon
     */
    private String _remoteUrl;

    public LazyImageLabel() {
        try {
            if ( !s_cachedIcons.containsKey(s_loadingGifKey)) {
                URL s_loadingGif = getClass().getResource("lazyPlaceholder.gif");
                s_cachedIcons.put(s_loadingGifKey, ImageIO.read(s_loadingGif));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lazily loads an image and sets it as the icon; displays a placeholder while icon is being loaded.
     * Sets the height and width to ({@link LazyImageLabel#_iconWidth}, {@link LazyImageLabel#_iconHeight}.
     * @param remoteUrl The url of the new icon
     */
    public void setIcon(final String remoteUrl) {
        setIcon(remoteUrl, _iconWidth, _iconHeight);
    }

    /**
     * Lazily loads an image and sets it as the icon; displays a placeholder while icon is being loaded.
     * @param remoteUrl The url of the new icon
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public void setIcon(final String remoteUrl, final int width, final int height) {
        _remoteUrl = remoteUrl;
        _iconHeight = height;
        _iconWidth = width;

        logger.trace("Initializing new lazy image label with url {}, width {}, height {}", remoteUrl, width, height);

        String imageKey = s_loadingGifKey;
        // if image has already been cached, no need to display placeholder
        if( s_cachedIcons.containsKey(_remoteUrl)) {
            imageKey = _remoteUrl;
        }

        super.setIcon(new ImageIcon(s_cachedIcons.get(imageKey).getScaledInstance(_iconWidth, _iconHeight, Image.SCALE_SMOOTH)));

        // if placeholder was shown, load the real image
        if( imageKey.equals(s_loadingGifKey) ) {
            new Thread(this).start();
        }
    }

    /**
     * Background thread to load the icon
     */
    @Override
    public void run() {
        try {
            if( !s_cachedIcons.containsKey(_remoteUrl)) {
                s_cachedIcons.put(_remoteUrl, ImageIO.read(new URL(_remoteUrl)));
            }
            this.setIcon(new ImageIcon(s_cachedIcons.get(_remoteUrl).getScaledInstance(_iconWidth, _iconHeight, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
