package com.lawrence254.datapipeline.config;

import com.lawrence254.datapipeline.repository.TimeSeriesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {
    private final TimeSeriesRepository timeSeriesRepository;

    @PostConstruct
    public void initializeDatabase() {
        timeSeriesRepository.createHyperTableSql();
        timeSeriesRepository.setupContinuousAggregation();
    }
}
