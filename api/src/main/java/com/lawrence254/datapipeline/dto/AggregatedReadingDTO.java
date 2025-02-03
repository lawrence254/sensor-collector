package com.lawrence254.datapipeline.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AggregatedReadingDTO {
    private String sessionId;
    private Instant windowStart;
    private Instant windowEnd;
    private double minValue;
    private double maxValue;
    private double stdDev;
    private long sampleCount;
    private String sensorType;

}
