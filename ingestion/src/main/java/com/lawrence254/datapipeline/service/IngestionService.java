package com.lawrence254.datapipeline.service;

import com.lawrence254.datapipeline.exception.DataPipelineException;
import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.util.MetricsUtils;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class IngestionService {
    private static final Logger logger = LoggerFactory.getLogger(IngestionService.class);
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final MetricsUtils metrics;
    private final String TOPIC = "sensor-readings";

    @Timed(value = "ingestion.kafka.send", description = "Time taken to send message to Kafka")
    public void processReading(SensorReading reading) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<SendResult<String, Object>> future= kafkaTemplate.send(TOPIC,reading.getSensorID(),reading);

        future.whenComplete((result, ex)->{
            if (ex != null){
                logger.error("Failed to send reading to kafka: {}", ex.getMessage(), ex);
                metrics.incrementErrorCount(
                        "kafka_producer",
                        System.currentTimeMillis()- startTime,
                        "producer_error"
                );
                throw new DataPipelineException(
                        DataPipelineException.ErrorCode.COMMUNICATION_ERROR,
                        "Failed to send sensor reading to kafka: "+ex.getMessage(),
                        ex
                );
            }else {
                logger.debug("Successfully sent reading from sensor {} to kafka", reading.getSensorID());
                metrics.recordProcessingTime(
                        "kafka_producer",
                        System.currentTimeMillis()-startTime,
                        reading.getSensorID()
                );
            }
        });
    }
}
