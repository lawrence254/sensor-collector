package com.lawrence254.datapipeline.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Entity
@Data
@Table(name="sensor_readings")
public class SensorReadingEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String sensorId;
    private Instant timestamp;
    private double value;
    private String sensorType;

    @ElementCollection
    @CollectionTable(name = "sensor_reading_metadata")
    @MapKeyColumn(name="key")
    @Column(name="value")
    private Map<String, String> metadata;

    @Embedded
    private LocationMetadataEmbeddable locationMetadataEmbeddable;
}

