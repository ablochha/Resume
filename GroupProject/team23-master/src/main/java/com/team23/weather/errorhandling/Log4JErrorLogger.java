package com.team23.weather.errorhandling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This object uses the error-handling logic provided by {@link IErrorHandler} and {@link com.team23.weather.App}
 * to respond to error events as they occur. In this case, errors are logged using {@link Logger}.
 *
 * @author Saquib Mian
 */
public class Log4JErrorLogger implements IErrorHandler {
    static Logger logger = LogManager.getLogger(Log4JErrorLogger.class.getName());

    /**
     * Overridden to log error messages using {@link Logger}.
     * @param e The error that occurred.
     */
    @Override
    public void handleError(Error e) {
        logger.error(e.getMessage(), e.getException());
    }
}
