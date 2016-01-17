package com.team23.weather.stream;

import java.io.*;

/**
 * The {@link com.team23.weather.stream.BetterInputStreamReader} is used to
 * completely read a stream as a String. The {@link java.io.InputStreamReader}
 * only supports reading in chunks of characters at a time, and is verbose.
 * This wraps {@link java.io.InputStreamReader} and reads the entire stream
 * at once.
 *
 * @author Saquib Mian
 */
public class BetterInputStreamReader {

    /**
     * the input stream that will be read from
     */
    private final InputStream _stream;

    /**
     * the default buffer size
     */
    private static final int s_bufferSize = 1024; // 1024 bytes

    /**
     * the default encoding for this string reader
     */
    public static final String defaultEncoding = "UTF-8";

    /**
     * @param stream the stream to completely read in from
     */
    public BetterInputStreamReader(InputStream stream) {
        _stream = stream;
    }

    /**
     * Reads the stream using the {@link com.team23.weather.stream.BetterInputStreamReader#defaultEncoding} encoding
     * @return the contents of the stream
     */
    @Override
    public String toString() {
        return toString(defaultEncoding);
    }

    /**
     * Reads the stream using the specified encoding
     * @param encoding The specified encoding.
     * @return the contents of the stream
     */
    public String toString(String encoding) {
        final char[] buffer = new char[s_bufferSize];
        final StringBuilder out = new StringBuilder(s_bufferSize);
        try {
            final Reader in = new BufferedReader(new InputStreamReader(_stream, encoding));
            try {
                int bytesRead;
                while ((bytesRead = in.read(buffer, 0, buffer.length)) >= 0) {
                    out.append(buffer, 0, bytesRead);
                }
            } finally {
                in.close();
            }
        } catch (IOException ex) {
        }

        return out.toString();
    }
}
