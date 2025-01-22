package com.lawrence254.datapipeline.util;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsUtils {
    private final MeterRegistry meterRegistry;

    public MetricsUtils(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordProcessingTime(String operationName, long milliseconds, String sensorId) {
        meterRegistry.timer("processing_time",
                List.of(
                        Tag.of("operation", operationName),
                        Tag.of("sensor_id", sensorId)
                )
        ).record(milliseconds, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void incrementErrorCount(String operationName, long milliseconds, String errorType) {
        meterRegistry.counter("processing_errors",
                List.of(
                        Tag.of("operation_name", operationName),
                        Tag.of("error_type", errorType)
                )
                ).increment();
    }

    public void recordSensorReading(String sensorId, double value, String sensorType) {
        meterRegistry.gauge("sensor_reading",
                List.of(
                        Tag.of("sensor_id", sensorId),
                        Tag.of("sensor_type", sensorType)
                ), value);
    }
}
