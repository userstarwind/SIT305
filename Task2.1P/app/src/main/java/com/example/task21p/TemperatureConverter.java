package com.example.task21p;

public class TemperatureConverter implements IUnitConverter {
    @Override
    public Double convert(Double value, String fromUnit, String toUnit) {
        if ("Celsius".equals(fromUnit)) {
            if ("Fahrenheit".equals(toUnit)) {
                return value * 9 / 5 + 32;
            } else if ("Kelvin".equals(toUnit)) {
                return value + 273.15;
            }
        } else if ("Fahrenheit".equals(fromUnit)) {
            if ("Celsius".equals(toUnit)) {
                return (value - 32) * 5 / 9;
            } else if ("Kelvin".equals(toUnit)) {
                return (value - 32) * 5 / 9 + 273.15;
            }
        } else if ("Kelvin".equals(fromUnit)) {
            if ("Celsius".equals(toUnit)) {
                return value - 273.15;
            } else if ("Fahrenheit".equals(toUnit)) {
                return (value - 273.15) * 9 / 5 + 32;
            }
        }
        return value; // Original value returned
    }
}
