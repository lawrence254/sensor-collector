package com.lawrence254.datapipeline.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({KafkaConfig.class, MetricsConfig.class})
public class IngestionConfig {
}
