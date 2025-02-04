package com.lawrence254.datapipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.lawrence254.datapipeline.api",
                "com.lawrence254.datapipeline.common",
                "com.lawrence254.datapipeline.ingestion",
                "com.lawrence254.datapipeline.monitoring",
                "com.lawrence254.datapipeline.processing",
                "com.lawrence254.datapipeline.storage"
        }
)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}