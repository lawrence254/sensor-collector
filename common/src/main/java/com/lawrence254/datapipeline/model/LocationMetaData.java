package com.lawrence254.datapipeline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMetaData {
    private String city;
    private Double longitude;
    private Double latitude;
}
