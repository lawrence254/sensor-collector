package com.lawrence254.datapipeline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedReading {
    private String sessionId;
    private Instant windowStart;
    private Instant windowEnd;
    private double avgValue;
    private double minValue;
    private double maxValue;
    private double stdDev;
    private long sampleCount;
    private SensorReading.SensorType sensorType;
}
