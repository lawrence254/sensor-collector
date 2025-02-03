package com.lawrence254.datapipeline.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "aggregated_readings")
public class AggregatedReadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private Instant windowStart;
    private Instant windowEnd;
    private double avgValue;
    private double minValue;
    private double maxValue;
    private double stdDev;
    private long sampleCount;
    private String sensorType;
}
