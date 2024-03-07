package com.example.task21p;

public class LengthConverter implements IUnitConverter {
    @Override
    public Double convert(Double value, String fromUnit, String toUnit) {
        double valueInMeters;
        switch (fromUnit) {
            case "inch":
                valueInMeters = value * 0.0254;
                break;
            case "foot":
                valueInMeters = value * 0.3048;
                break;
            case "yard":
                valueInMeters = value * 0.9144;
                break;
            case "mile":
                valueInMeters = value * 1609.34;
                break;
            case "cm":
                valueInMeters = value * 0.01;
                break;
            case "km":
                valueInMeters = value * 1000;
                break;
            default:
                return value;
        }

        switch (toUnit) {
            case "inch":
                return valueInMeters / 0.0254;
            case "foot":
                return valueInMeters / 0.3048;
            case "yard":
                return valueInMeters / 0.9144;
            case "mile":
                return valueInMeters / 1609.34;
            case "cm":
                return valueInMeters / 0.01;
            case "km":
                return valueInMeters / 1000;
            default:
                return valueInMeters;
        }
    }
}

