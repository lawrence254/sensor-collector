package com.lawrence254.datapipeline.health;

import com.lawrence254.datapipeline.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageHealthIndicator implements HealthIndicator {
    private final StorageService storageService;
    @Override
    public Health health() {
        try {
            storageService.checkConnection();
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}
