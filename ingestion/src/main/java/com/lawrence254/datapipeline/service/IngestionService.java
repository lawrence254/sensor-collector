package com.lawrence254.datapipeline.service;

import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.util.MetricsUtils;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class IngestionService {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final MetricsUtils metrics;
    private final String TOPIC = "sensor-readings";

    @Timed(value = "ingestion.kafka.send", description = "Time taken to send message to Kafka")
    public void processReading(SensorReading reading) {
        long startTime = System.currentTimeMillis();
        try {
            kafkaTemplate.send(TOPIC, reading.getSensorID(),reading);
            metrics.recordProcessingTime(
                    "kafka_producer",
                    System.currentTimeMillis()-startTime,
                    reading.getSensorID()
            );
        }catch (Exception e){
            metrics.incrementErrorCount(
                    "kafka_producer",
                    System.currentTimeMillis()-startTime,
                    "PRODUCER_ERROR"
            );
            throw  e;
        }
    }
}
