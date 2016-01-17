package com.team23.weather.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class TemperatureTest {

    private final double _errorMargin = 0.01;

    @Test
    public void getValueForUnit__fromKelvin__returnsCorrectConversions() throws Exception {
        Temperature temp = new Temperature(10, TemperatureUnit.Kelvin);

        assertEquals(10, temp.getValueForUnit(TemperatureUnit.Kelvin), _errorMargin);
        assertEquals(-263.15, temp.getValueForUnit(TemperatureUnit.Celsius), _errorMargin);
        assertEquals(-441.67, temp.getValueForUnit(TemperatureUnit.Farenheit), _errorMargin);
    }

    @Test
    public void getValueForUnit__fromCelsius__returnsCorrectConversions() throws Exception {
        Temperature temp = new Temperature(10, TemperatureUnit.Celsius);

        assertEquals(283.15, temp.getValueForUnit(TemperatureUnit.Kelvin), _errorMargin);
        assertEquals(10, temp.getValueForUnit(TemperatureUnit.Celsius), _errorMargin);
        assertEquals(50, temp.getValueForUnit(TemperatureUnit.Farenheit), _errorMargin);
    }

    @Test
    public void getValueForUnit__fromFarenheit__returnsCorrectConversions() throws Exception {
        Temperature temp = new Temperature(10, TemperatureUnit.Farenheit);

        assertEquals(260.93, temp.getValueForUnit(TemperatureUnit.Kelvin), _errorMargin);
        assertEquals(-12.22, temp.getValueForUnit(TemperatureUnit.Celsius), _errorMargin);
        assertEquals(10, temp.getValueForUnit(TemperatureUnit.Farenheit), _errorMargin);
    }
}