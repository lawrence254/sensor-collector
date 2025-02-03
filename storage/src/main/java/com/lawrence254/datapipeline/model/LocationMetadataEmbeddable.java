package com.lawrence254.datapipeline.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
public class LocationMetadataEmbeddable{
    private String city;
    private Double longitude;
    private Double latitude;
}
