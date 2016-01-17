package com.team23.weather.errorhandling;

/**
 * The {@link Error} model captures any data that is available when an error occurs.
 *
 * This model is primarily used by the {@link com.team23.weather.ScrollableGui} class to power the error bar,
 * and the {@link Log4JErrorLogger} to log any errors that occur.
 *
 * @author Saquib Mian
 */
public class Error {
    /**
     * The error message
     */
    private String _message;

    /**
     * The error's throwing origin
     */
    private Object _origin;

    /**
     * The exception associated with the error, if there is one
     */
    private Throwable _exception;

    public Error(String message, Object origin, Throwable exception) {
        _message = message;
        _origin = origin;
        _exception = exception;
    }

    /**
     * Get the error's message
     * @return the error's message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Get the error's throwing origin
     * @return the error's throwing origin
     */
    public Object getOrigin() {
        return _origin;
    }

    /**
     * Gets the exception associated with the error
     * @return the exception associated with the error
     */
    public Throwable getException() {
        return _exception;
    }
}
