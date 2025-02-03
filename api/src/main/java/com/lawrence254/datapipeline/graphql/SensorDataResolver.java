package com.lawrence254.datapipeline.graphql;

import com.lawrence254.datapipeline.dto.AggregatedReadingDTO;
import com.lawrence254.datapipeline.dto.SensorReadingDTO;
import com.lawrence254.datapipeline.model.AggregatedReadingEntity;
import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.model.SensorReadingEntity;
import com.lawrence254.datapipeline.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SensorDataResolver {
    private final StorageService storageService;

    @QueryMapping
    public List<SensorReadingDTO> sensorReadings(@Argument String sensorId,
                                                 @Argument String startTime,
                                                 @Argument String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);

        return storageService.getReadingForSensor(sensorId, start, end).stream().map(this::mapToDTO).toList();

    }

    @QueryMapping
    public List<AggregatedReadingDTO> aggregatedReadings(@Argument String sessionId,
                                                         @Argument String startTime,
                                                         @Argument String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);

        return storageService.getAggregatedReadings(sessionId, start, end).stream().map(this::mapToAggregateDTO).toList();
    }

    private AggregatedReadingDTO mapToAggregateDTO(AggregatedReadingEntity aggregatedReadingEntity) {
        AggregatedReadingDTO aggregatedReadingDTO = new AggregatedReadingDTO();

        return aggregatedReadingDTO;
    }

    private SensorReadingDTO mapToDTO(SensorReadingEntity reading) {
        SensorReadingDTO dto = new SensorReadingDTO();

        return dto;
    }
}
