package com.lawrence254.datapipeline.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading{
    @NotNull
    private String sensorID;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZ", timezone = "UTC")
    private Instant timestamp;
    private double value;
    Map<String, String> metadata;
    private SensorType sensorType;
    private LocationMetaData locationMetaData;

    public enum SensorType {
        TEMPERATURE,
        HUMIDITY,
        PRESSURE,
        VIBRATION,
        VOLTAGE,
        CO,
        CO2,
        PM1,
        PM10
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private class LocationMetaData {
        private String city;
        private Double longitude;
        private Double latitude;
    }
}
