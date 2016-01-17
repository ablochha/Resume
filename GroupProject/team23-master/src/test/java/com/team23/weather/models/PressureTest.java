package com.team23.weather.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class PressureTest {

    private final double _errorMargin = 0.01;

    @Test
    public void getValueForUnit__formPascal__returnsCorrectConversions() {
        Pressure pressure = new Pressure(100000, PressureUnit.Pascal);

        assertEquals(100000, pressure.getValueForUnit(PressureUnit.Pascal), _errorMargin);
        assertEquals(750.06, pressure.getValueForUnit(PressureUnit.Mercury), _errorMargin);
    }
    @Test
    public void getValueForUnit__formMercury__returnsCorrectConversions() {
        Pressure pressure = new Pressure(100000, PressureUnit.Mercury);

        assertEquals(100000, pressure.getValueForUnit(PressureUnit.Mercury), _errorMargin);
        assertEquals(13332236.83, pressure.getValueForUnit(PressureUnit.Pascal), _errorMargin);
    }

}