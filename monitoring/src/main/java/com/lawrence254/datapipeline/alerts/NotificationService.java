package com.lawrence254.datapipeline.alerts;

import com.lawrence254.datapipeline.metrics.SensorMetrics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final SensorMetrics sensorMetrics;
    public void sendNotification(String message) {
        logger.error("ALERT : {}",message);
        String sensorId = extractSensorId(message);

        // Increment anomaly counter for the specific sensor
        if (sensorId != null) {
            sensorMetrics.incrementAnomalyCounter(sensorId, "alerts");
        } else {
            // Fallback to system-wide counter if no specific sensor is identified
            sensorMetrics.incrementAnomalyCounter("system", "alerts");
        }
    }

    private String extractSensorId(String message) {
        // Extract sensor ID from the message format
        // "High anomaly count detected for sensor: {sensorId}"
        String prefix = "High anomaly count detected for sensor: ";
        if (message != null && message.startsWith(prefix)) {
            return message.substring(prefix.length());
        }
        return null;
    }
}
