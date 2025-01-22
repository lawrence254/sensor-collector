package com.lawrence254.datapipeline.util;

import com.lawrence254.datapipeline.model.SensorReading;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ValidationUtils {
    public boolean isValidReading(SensorReading reading) {
        if (reading == null) return false;

        // Checks if the timestamp is too old or in the future
        Instant now = Instant.now();
        Instant threshold = now.minus(24, ChronoUnit.HOURS);

        if(reading.getTimestamp().isAfter(now) || reading.getTimestamp().isBefore(threshold)) {
            return false;
        }

        return switch (reading.getSensorType()){
            case TEMPERATURE -> isValidTemperature(reading.getValue());
            case HUMIDITY -> isValidHumidity(reading.getValue());
            case PRESSURE -> isValidPressure(reading.getValue());
            case VIBRATION -> isValidVibration(reading.getValue());
            case VOLTAGE -> isValidVoltage(reading.getValue());
            case CO -> isValidCOReading(reading.getValue());
            case CO2 -> isValidCO2Reading(reading.getValue());
            case PM1 -> isValidPM1Reading(reading.getValue());
            case PM10 -> isValidPM10Reading(reading.getValue());
        };
    }

    private boolean isValidCOReading(double value) {
        return value >= 0 && value <= 500;
    }

    private boolean isValidCO2Reading(double value) {
        return value >= 0 && value <= 10000;
    }

    private boolean isValidPM10Reading(double value) {
        return value >= 0 && value <= 31000;
    }

    private boolean isValidPM1Reading(double value) {
        return value >= 0 && value <= 2000;
    }

    private boolean isValidTemperature(double value) {
        return value >= -50 && value <= 150; // °C
    }

    private boolean isValidHumidity(double value) {
        return value >= 0 && value <= 100; // %
    }

    private boolean isValidPressure(double value) {
        return value >= 800 && value <= 1200; // hPa
    }

    private boolean isValidVibration(double value) {
        return value >= 0 && value <= 100; // mm/s
    }

    private boolean isValidVoltage(double value) {
        return value >= 0 && value <= 500; // V
    }
}