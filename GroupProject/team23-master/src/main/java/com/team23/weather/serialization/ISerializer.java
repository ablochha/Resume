package com.team23.weather.serialization;

import java.io.Reader;

/**
 * This interface provides a wrapper which any object serializer must implement.
 * Examples include {@link com.team23.weather.serialization.JsonSerializer}.
 *
 * @author Saquib Mian
 */
public interface ISerializer {

    /**
     * Deserialize the {@link T} object from a {@link java.lang.String}.
     * @param json the serialized {@link java.lang.String} representation of {@link T}
     * @param classType the type of {@link T}
     * @param <T> the type of object to deserialize the {@link java.lang.String} into
     * @return the {@link T} object as it was persisted
     */
    <T> T deserialize( String json, Class<T> classType );

    /**
     * Deserialize the {@link T} object from a {@link java.io.Reader} to the underlying binary data.
     * @param stream a {@link java.io.Reader} for the underlying binary representation of {@link T}
     * @param classType the type of {@link T}
     * @param <T> the type of object to deserialize the underlying binary representation from
     * @return the {@link T} object as it was persisted
     */
    <T> T deserialize( Reader stream, Class<T> classType );

    /**
     * Serialize the {@link T} object into a {@link java.lang.String}.
     * @param obj a {@link T} object to serialize
     * @param <T> the type of object to serialize the underlying binary representation into
     * @return the {@link java.lang.String} representation of the object
     */
    <T> String serialize( T obj );

}
