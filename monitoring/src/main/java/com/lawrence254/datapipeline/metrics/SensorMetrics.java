package com.lawrence254.datapipeline.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SensorMetrics {
    private final MeterRegistry registry;

    public void recordSensorReading(String sensorId, double value, String type) {
        registry.gauge(
                "sensor.reading",
                List.of(
                        Tag.of("sensor_id", sensorId),
                        Tag.of("type", type)
                ),
                value
        );
    }

    public void incrementAnomalyCounter(String sensorId, String type) {
        registry.counter("sensor.anomalies",
                List.of(
                        Tag.of("sensor_id", sensorId),
                        Tag.of("type", type)
                )
        ).increment();
    }
}
