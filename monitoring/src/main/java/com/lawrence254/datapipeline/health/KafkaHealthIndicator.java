package com.lawrence254.datapipeline.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaHealthIndicator implements HealthIndicator {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Health health() {
        try{
            kafkaTemplate.getProducerFactory().createProducer().metrics();
            return Health.up().build();
        }catch (Exception e){
            return Health.down().withException(e).build();
        }
    }
}
