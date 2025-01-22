package com.lawrence254.datapipeline.enrichment;

import com.lawrence254.datapipeline.model.SensorReading;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EnrichmentService {
    private final Map<String, Map<String, String>> sensorMetadata = new ConcurrentHashMap<>();

    public SensorReading enrichReading(SensorReading reading){
        Map<String, String> metadata = sensorMetadata.getOrDefault(
                reading.getSensorID(),
                Map.of()
        );
        return SensorReading.builder()
                .sensorID(reading.getSensorID())
                .timestamp(reading.getTimestamp())
                .value(reading.getValue())
                .sensorType(reading.getSensorType())
                .metadata(metadata)
                .locationMetaData(reading.getLocationMetaData())
                .build();
    }

    public void updateSensorMetadata(String sensorId, Map<String, String> metadata){
        sensorMetadata.put(sensorId, metadata);
    }
}
