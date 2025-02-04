package com.lawrence254.datapipeline.alerts;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertingService {
    private final MeterRegistry registry;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkAnomalyThreshold() {
        registry.find("sensor.anomalies")
                .counters()
                .forEach(counter -> {
                    if (counter.count()>10){
                        notificationService.sendNotification(
                                "High anomaly count detected for sensor: "+ counter.getId().getTag("sensor_id")
                        );
                    }
                });
    }
}
