package com.lawrence254.datapipeline.interfaces;

import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.model.AggregatedReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface AggregatedReadingRepository extends JpaRepository<AggregatedReadingEntity, Long> {
    List<AggregatedReadingEntity> findBySessionIdAndWindowStartBetween(String sessionId, Instant windowStartAfter, Instant windowStartBefore);
}
