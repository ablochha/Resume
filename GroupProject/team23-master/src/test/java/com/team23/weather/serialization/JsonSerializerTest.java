package com.team23.weather.serialization;

import com.google.gson.Gson;
import com.team23.weather.models.GeoCoordinate;
import com.team23.weather.models.Location;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class JsonSerializerTest {

    private final JsonSerializer _jsonSerializer = new JsonSerializer();
    private Location _deserialized;
    private String _serialized;

    private final double _errorMargin = 0.01;

    @Before
    public void beforeEach() {
        _deserialized = new Location(0, "London", "CA", new GeoCoordinate(0, 0));
        _serialized = new Gson().toJson(_deserialized);

        assertNotNull("Location is null", _deserialized);
        assertNotNull("JSON is null", _serialized);
    }


    @Test
    public void deserialize__string__returnsCorrectObject() throws Exception {
        Location deserialized = _jsonSerializer.deserialize(_serialized, _deserialized.getClass());

        assertEquals(_deserialized.getCity(), deserialized.getCity());
        assertEquals(_deserialized.getCountry(), deserialized.getCountry());
        assertEquals(_deserialized.getLocationId(), deserialized.getLocationId());
        assertEquals(_deserialized.getPosition().getLatitude(), deserialized.getPosition().getLatitude(), _errorMargin);
        assertEquals(_deserialized.getPosition().getLongitude(), deserialized.getPosition().getLongitude(), _errorMargin);
    }

    @Test
    public void deserialize__stream__returnsCorrectObject() throws Exception {
        Reader stream = new StringReader(_serialized);

        Location deserialized = _jsonSerializer.deserialize(stream, _deserialized.getClass());

        assertEquals(_deserialized.getCity(), deserialized.getCity());
        assertEquals(_deserialized.getCountry(), deserialized.getCountry());
        assertEquals(_deserialized.getLocationId(), deserialized.getLocationId());
        assertEquals(_deserialized.getPosition().getLatitude(), deserialized.getPosition().getLatitude(), _errorMargin);
        assertEquals(_deserialized.getPosition().getLongitude(), deserialized.getPosition().getLongitude(), _errorMargin);
    }

    @Test
    public void serialize__object__serializesAllFields() throws Exception {
        assertEquals(_serialized, _jsonSerializer.serialize(_deserialized));
    }
}