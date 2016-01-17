package com.team23.weather.serialization;

import com.google.gson.Gson;

import java.io.Reader;

/**
 * This class implements {@link com.team23.weather.serialization.ISerializer}, using JSON as the serialization format
 *
 * @author Saquib Mian
 */
public class JsonSerializer implements ISerializer {

    /**
     * The Google JSON serializer
     */
    private final Gson _gson;

    public JsonSerializer() {
        _gson = new Gson();
    }

    /**
     * {@inheritDoc}
     * The underlying format this {@link com.team23.weather.serialization.ISerializer} is JSON
     */
    @Override
    public <T> T deserialize(String json, Class<T> classType ) {
        return _gson.fromJson( json, classType );
    }

    /**
     * {@inheritDoc}
     * The underlying format this {@link com.team23.weather.serialization.ISerializer} is JSON
     */
    @Override
    public <T> T deserialize(Reader stream, Class<T> classType) {
        return _gson.fromJson( stream, classType );
    }

    /**
     * {@inheritDoc}
     * The underlying format this {@link com.team23.weather.serialization.ISerializer} is JSON
     */
    @Override
    public <T> String serialize(T obj) {
        return _gson.toJson( obj );
    }
}
