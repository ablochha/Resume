package com.team23.weather.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpeedTest {

    private final double _errorMargin = 0.01;

    @Test
    public void getValueForUnit__fromMPH__returnsCorrectConversions() throws Exception {
        Speed speed = new Speed(10, SpeedUnit.MPH);

        assertEquals(10, speed.getValueForUnit(SpeedUnit.MPH), _errorMargin);
        assertEquals(6.21, speed.getValueForUnit(SpeedUnit.KMPH), _errorMargin);
    }

    @Test
    public void getValueForUnit__fromKMPH__returnsCorrectConversions() throws Exception {
        Speed speed = new Speed(10, SpeedUnit.KMPH);

        assertEquals(10, speed.getValueForUnit(SpeedUnit.KMPH), _errorMargin);
        assertEquals(16.09, speed.getValueForUnit(SpeedUnit.MPH), _errorMargin);
    }

}