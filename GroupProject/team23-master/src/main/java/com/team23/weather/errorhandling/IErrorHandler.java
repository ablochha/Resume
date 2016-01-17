package com.team23.weather.errorhandling;

/**
 * This interface allows objects to handle application-level errors as they occur.
 *
 * Errors cannot be dismissed, and handlers will be fired in the order in which
 * they are registered. Registration occurs with {@link com.team23.weather.App}.
 *
 * @author Saquib Mian
 */
public interface IErrorHandler {
    /**
     * The main error-handling method.
     * @param e The error that occurred.
     */
    void handleError(Error e);
}
