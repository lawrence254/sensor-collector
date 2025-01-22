package com.lawrence254.datapipeline.controller;

import com.lawrence254.datapipeline.exception.DataPipelineException;
import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.service.IngestionService;
import com.lawrence254.datapipeline.util.MetricsUtils;
import com.lawrence254.datapipeline.util.ValidationUtils;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorReadingController {
    private static final Logger logger = LoggerFactory.getLogger(SensorReadingController.class);
    private final IngestionService ingestionService;
    private final ValidationUtils validationUtils;
    private final MetricsUtils metricsUtils;
    private final PrometheusMeterRegistry registry;

    @PostMapping("/readings")
    @Timed(value = "ingestion.reading.process", description = "Time taken to process a sensor reading")
    public ResponseEntity<?> ingestReading(@Valid @RequestBody SensorReading reading){
        long startTime = System.currentTimeMillis();

        try{
            if(!validationUtils.isValidReading(reading)){
                metricsUtils.incrementErrorCount(
                        "reading_validation",
                        System.currentTimeMillis()-startTime,
                        "validation_failure"
                );
                throw new DataPipelineException(DataPipelineException.ErrorCode.INVALID_SENSOR_DATA,
                        "Invalid sensor data for sensor: "+reading.getSensorID());
            }

            ingestionService.processReading(reading);

            metricsUtils.recordSensorReading(
                    reading.getSensorID(),
                    reading.getValue(),
                    reading.getSensorType().toString()
            );

            metricsUtils.recordProcessingTime(
                    "reading_ingestion",
                    System.currentTimeMillis()-startTime,
                    reading.getSensorID()
            );
            return ResponseEntity.accepted().build();
        }catch (DataPipelineException e){
            logger.error("Data pipeline error during ingestation: {}", e.getMessage(),e);
            metricsUtils.incrementErrorCount(
                    "reading_ingestion",
                    System.currentTimeMillis()-startTime,
                    "system_error"
            );
            throw new DataPipelineException(
                    DataPipelineException.ErrorCode.PROCESSING_ERROR,
                    "Failed to process sensor reading: "+e.getMessage(),e
            );
        }
    }

    @GetMapping("/metrics")
    public String metrics(){
        return registry.scrape();
    }

}
