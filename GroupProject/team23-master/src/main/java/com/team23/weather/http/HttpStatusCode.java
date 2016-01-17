package com.team23.weather.http;

/**
 * This class represents any of the HTTP Status Code used by {@link com.team23.weather.http.HttpClient}
 *
 * @author Saquib Mian
 */
public enum HttpStatusCode {
    /**
     * The 'OK' status code
     */
    OK(200);

    /**
     * The integer representation of the HTTP Status Code
     */
    private final int _value;

    private HttpStatusCode(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }
}
