package com.team23.weather.http;

import com.team23.weather.stream.BetterInputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class acts like a web client to make web requests with. It only supports GET requests.
 *
 * @author Saquib Mian
 */
public class HttpClient {

    static Logger logger = LogManager.getLogger(HttpClient.class.getName());

    /**
     * Sends a GET request of the URL and returns a {@link java.io.InputStream} of the response
     * @param url The URL to open a GET request stream to
     * @return The {@link java.io.InputStream} response stream
     * @throws IOException if the response code is not {@link com.team23.weather.http.HttpStatusCode#OK}, if the URL is invalid, or if the web server has an issue
     */
    public InputStream get(String url) throws IOException {
        logger.trace("GET " + url);

        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();

        if (connection.getResponseCode() != HttpStatusCode.OK.getValue()) {
            throw new IOException("Response code was: " + connection.getResponseCode());
        }

        return connection.getInputStream();
    }

    /**
     * Sends a GET request of the URL and reads the entire response back as a {@link java.lang.String}
     * @param url The URL to send a GET request to
     * @return The response
     * @throws IOException if the response code is not {@link com.team23.weather.http.HttpStatusCode#OK}, if the URL is invalid, or if the web server has an issue
     */
    public String getAsString(String url) throws IOException {
        BetterInputStreamReader reader = new BetterInputStreamReader(get(url));

        return reader.toString();
    }

}
