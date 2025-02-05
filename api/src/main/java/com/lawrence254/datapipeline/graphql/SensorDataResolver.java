package com.lawrence254.datapipeline.graphql;

import com.lawrence254.datapipeline.dto.AggregatedReadingDTO;
import com.lawrence254.datapipeline.dto.LocationMetadataDTO;
import com.lawrence254.datapipeline.dto.SensorReadingDTO;
import com.lawrence254.datapipeline.model.AggregatedReadingEntity;
import com.lawrence254.datapipeline.model.LocationMetaData;
import com.lawrence254.datapipeline.model.SensorReading;
import com.lawrence254.datapipeline.model.SensorReadingEntity;
import com.lawrence254.datapipeline.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        return storageService.getAggregatedReadings(sessionId, start, end).stream().map(this::mapToAggregatedDTO).toList();
    }

    private SensorReadingDTO mapToDTO(SensorReadingEntity entity) {
        if (entity == null) {
            return null;
        }

        SensorReadingDTO dto = new SensorReadingDTO();
        dto.setSensorId(entity.getSensorId());
        dto.setTimestamp(entity.getTimestamp());
        dto.setValue(entity.getValue());
        dto.setSensorType(entity.getSensorType());
        dto.setMetadata(entity.getMetadata());

        // Map location metadata if present
        if (entity.getLocationMetadataEmbeddable() != null) {
            LocationMetaData locationDTO = new LocationMetaData();
            locationDTO.setCity(entity.getLocationMetadataEmbeddable().getCity());
            locationDTO.setLongitude(entity.getLocationMetadataEmbeddable().getLongitude());
            locationDTO.setLatitude(entity.getLocationMetadataEmbeddable().getLatitude());
            dto.setLocation(locationDTO);
        }

        return dto;
    }

    private AggregatedReadingDTO mapToAggregatedDTO(AggregatedReadingEntity entity) {
        if (entity == null) {
            return null;
        }

        AggregatedReadingDTO dto = new AggregatedReadingDTO();
        dto.setSessionId(entity.getSessionId());
        dto.setWindowStart(entity.getWindowStart());
        dto.setWindowEnd(entity.getWindowEnd());
        dto.setAvgValue(entity.getAvgValue());
        dto.setMinValue(entity.getMinValue());
        dto.setMaxValue(entity.getMaxValue());
        dto.setStdDev(entity.getStdDev());
        dto.setSampleCount(entity.getSampleCount());
        dto.setSensorType(entity.getSensorType());

        return dto;
    }

    // Optional: Add a mapper for collections
    private List<SensorReadingDTO> mapToSensorReadingDTOs(List<SensorReadingEntity> entities) {
        return Optional.ofNullable(entities)
                .map(list -> list.stream()
                        .map(this::mapToDTO)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private List<AggregatedReadingDTO> mapToAggregatedReadingDTOs(List<AggregatedReadingEntity> entities) {
        return Optional.ofNullable(entities)
                .map(list -> list.stream()
                        .map(this::mapToAggregatedDTO)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
