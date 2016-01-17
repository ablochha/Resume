package com.team23.weather.formatting;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains a set of convenience methods to help us format things, with all of the formatting logic in one place.
 *
 * @author Saquib Mian
 */
public class Formatter {

    /**
     * Format a {@link java.util.Date} as a full date, in the format "March 09, 2015"
     * @param date the {@link java.util.Date} to format
     * @return the formatted {@link java.util.Date}
     */
    public static String asFullDate(Date date) {
        return new SimpleDateFormat("MMMM dd YYYY").format(date);
    }

    /**
     * Format a {@link java.util.Date} as a time, in the format "1:00 PM"
     * @param date the {@link java.util.Date} to format
     * @return the formatted {@link java.util.Date}
     */
    public static String asTime(Date date) {
        return new SimpleDateFormat("h:mm a").format(date);
    }

    /**
     * Format a {@link java.util.Date} as a short date, in the format "Mar 09"
     * @param date the {@link java.util.Date} to format
     * @return the formatted {@link java.util.Date}
     */
    public static String asDate(Date date) {
        return new SimpleDateFormat("MMM dd").format(date);
    }
}
