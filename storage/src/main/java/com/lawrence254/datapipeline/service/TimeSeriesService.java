package com.lawrence254.datapipeline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.repository.TimeSeriesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSeriesService {
    private final TimeSeriesRepository timeSeriesRepository;

    @Transactional
    public void storeSensorReading(SensorReading sensorReading) throws JsonProcessingException {
        timeSeriesRepository.saveSensorReading(sensorReading);
    }

    @Transactional
    public void storeAggregateReading(AggregatedReading aggregatedReading) throws JsonProcessingException {
        timeSeriesRepository.saveAggregatedReading(aggregatedReading);
    }

    public List<SensorReading> getSensorReadings(String sensorId, Instant start, Instant end) throws JsonProcessingException {
        return timeSeriesRepository.getSensorReadings(sensorId, start, end);
    }

}
