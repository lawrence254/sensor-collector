package com.lawrence254.datapipeline.interfaces;

import com.lawrence254.datapipeline.model.SensorReadingEntity;
import org.apache.kafka.common.metrics.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReadingEntity, Long> {
    List<SensorReadingEntity> findSensorAndTimestampBetween(String sensorId, Instant start, Instant end);

    @Query("SELECT s FROM SensorReadingEntity s WHERE s.sensorType= :type AND s.value > :threshold")
    List<SensorReadingEntity> findAnamalousReadings(String type, double threshold);
}
