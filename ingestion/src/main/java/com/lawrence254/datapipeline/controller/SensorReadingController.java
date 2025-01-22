package com.lawrence254.datapipeline.controller;

import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.service.IngestionService;
import com.lawrence254.datapipeline.util.MetricsUtils;
import com.lawrence254.datapipeline.util.ValidationUtils;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
@AllArgsConstructor
public class SensorReadingController {

    private final IngestionService ingestionService;
    private final ValidationUtils validationUtils;
    private final MetricsUtils metricsUtils;

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
                return ResponseEntity.badRequest().body("Invalid Sensor Reading");
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
        }catch (Exception e){
            metricsUtils.incrementErrorCount(
                    "reading_ingestion",
                    System.currentTimeMillis()-startTime,
                    "system_error"
            );
            throw e;
        }
    }


}
