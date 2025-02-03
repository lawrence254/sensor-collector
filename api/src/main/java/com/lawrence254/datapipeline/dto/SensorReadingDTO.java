package com.lawrence254.datapipeline.dto;

import com.lawrence254.datapipeline.model.LocationMetaData;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class SensorReadingDTO {
    private String sensorId;
    private Instant timestamp;
    private double value;
    private String sensorType;
    private Map<String, String> metadata;
    private LocationMetaData location;
}
