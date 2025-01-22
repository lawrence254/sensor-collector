package com.lawrence254.datapipeline.analytics;

import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.util.MetricsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final MetricsUtils metricsUtils;

    public void processAggregatedReading(AggregatedReading reading){
        metricsUtils.recordSensorReading(
                reading.getSessionId(),
                reading.getAvgValue(),
                reading.getSensorType().toString()
        );
        detectAnomalies(reading);
    }

    private void detectAnomalies(AggregatedReading reading){
        double threshold = 2.0;
        double normalizedValue = (reading.getAvgValue()- reading.getMinValue())/ reading.getStdDev();

        if (Math.abs(normalizedValue)> threshold){
            metricsUtils.incrementErrorCount(
                    "anomaly_detection",
                    0,
                    "value_anomaly"
            );
        }
    }
}
