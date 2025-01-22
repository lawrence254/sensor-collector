package com.lawrence254.datapipeline.streams;

import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.model.SensorReading;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor

public class MetricAggregator {
    private final List<Double> values = new ArrayList<>();
    private double sum =0;
    private double squareSum = 0;
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private String sensorType;

    public MetricAggregator add(SensorReading reading){
        double value = reading.getValue();
        values.add(value);
        sum+=value;
        squareSum += value*value;
        min=Math.min(min, value);
        max=Math.max(max, value);
        sensorType=reading.getSensorType().toString();
        return this;
    }

    public AggregatedReading toAggregatedReading(Instant windowStart, Instant windowEnd){
        int count = values.size();
        double avg = sum/count;
        double variance = (squareSum/count)-(avg*avg);
        double stdDeviation=Math.sqrt(variance);

        return AggregatedReading.builder()
                .sessionId(windowStart.toString())
                .windowStart(windowStart)
                .windowEnd(windowEnd)
                .avgValue(avg)
                .minValue(min)
                .maxValue(max)
                .stdDev(stdDeviation)
                .sampleCount(count)
                .sensorType(SensorReading.SensorType.valueOf(sensorType))
                .build();
    }
}
