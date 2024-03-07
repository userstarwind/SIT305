package com.example.task21p;

public class WeightConverter implements IUnitConverter {
    @Override
    public Double convert(Double value, String fromUnit, String toUnit) {
        double valueInGrams;
        switch (fromUnit) {
            case "pound":
                valueInGrams = value * 453.59237;
                break;
            case "ounce":
                valueInGrams = value * 28.34952;
                break;
            case "ton":
                valueInGrams = value * 907185.0;
                break;
            case "kg":
                valueInGrams = value * 1000;
                break;
            case "g":
                valueInGrams = value;
                break;
            default:
                return value;
        }

        switch (toUnit) {
            case "pound":
                return valueInGrams / 453.59237;
            case "ounce":
                return valueInGrams / 28.34952;
            case "ton":
                return valueInGrams / 907185.0;
            case "kg":
                return valueInGrams / 1000;
            default:
                return valueInGrams;
        }
    }
}

