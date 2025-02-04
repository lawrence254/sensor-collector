package com.lawrence254.datapipeline.service;

import com.lawrence254.datapipeline.interfaces.AggregatedReadingRepository;
import com.lawrence254.datapipeline.interfaces.SensorReadingRepository;
import com.lawrence254.datapipeline.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final SensorReadingRepository sensorReadingRepository;
    private final AggregatedReadingRepository aggregatedReadingRepository;

    @Transactional
    public void storeSensorReading(SensorReading reading){
        SensorReadingEntity entity = new SensorReadingEntity();
        entity.setSensorId(reading.getSensorID());
        entity.setTimestamp(reading.getTimestamp());
        entity.setValue(reading.getValue());
        entity.setSensorType(reading.getSensorType().toString());
        entity.setMetadata(reading.getMetadata());

        LocationMetadataEmbeddable locationMetadata = new LocationMetadataEmbeddable();
        if (reading.getLocationMetaData() !=null){
            locationMetadata.setCity(reading.getLocationMetaData().getCity());
            locationMetadata.setLongitude(reading.getLocationMetaData().getLongitude());
            locationMetadata.setLatitude(reading.getLocationMetaData().getLatitude());
        }
        entity.setLocationMetadataEmbeddable(locationMetadata);

        sensorReadingRepository.save(entity);
    }

    @Transactional
    public void storeAggregatedReading(AggregatedReading reading){
        AggregatedReadingEntity entity = new AggregatedReadingEntity();

        entity.setSessionId(reading.getSessionId());
        entity.setWindowStart(reading.getWindowStart());
        entity.setWindowEnd(reading.getWindowEnd());
        entity.setAvgValue(reading.getAvgValue());
        entity.setMinValue(reading.getMinValue());
        entity.setMaxValue(reading.getMaxValue());
        entity.setStdDev(reading.getStdDev());
        entity.setSampleCount(reading.getSampleCount());
        entity.setSensorType(reading.getSensorType().toString());

        aggregatedReadingRepository.save(entity);

    }

    public List<SensorReadingEntity> getReadingForSensor(String sensorId, Instant start, Instant end){
        return sensorReadingRepository.findBySensorIdAndTimestampBetween(sensorId, start,end);

    }

    public List<AggregatedReadingEntity> getAggregatedReadings(String sessionId, Instant start, Instant end){
        return aggregatedReadingRepository.findBySessionIdAndWindowStartBetween(sessionId, start, end);
    }

    public void checkConnection() {

    }
}
