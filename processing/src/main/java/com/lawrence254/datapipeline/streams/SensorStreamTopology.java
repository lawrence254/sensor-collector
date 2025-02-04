package com.lawrence254.datapipeline.streams;

import com.lawrence254.datapipeline.analytics.AnalyticsService;
import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.model.SensorReading;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SensorStreamTopology {
    private final AnalyticsService analyticService;
    private static String INPUT_TOPIC = "sensor-readings";
    private static String OUTPUT_TOPIC = "aggregated-readings";

    /**
     * Configures and builds a Kafka Streams processing pipeline to process sensor readings,
     * aggregate data in time windows, and send the results to an output topic.
     *
     * @param streamsBuilder the StreamsBuilder used to build the Kafka Streams topology
     */
    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder){
        TimeWindows timeWindows = TimeWindows.of(Duration.ofMinutes(5))
                .advanceBy(Duration.ofMinutes(1));

        KStream<String, SensorReading> sensorStream = streamsBuilder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(),
                new JsonSerde<>(SensorReading.class)));

        sensorStream
                .groupBy((key, reading)-> reading.getSensorType().toString())
                .windowedBy(timeWindows)
                .aggregate(
                        ()->new MetricAggregator(),
                        (key, reading, aggregate)-> aggregate.add(reading),
                        Materialized.as("metric-store")
                )
                .toStream()
                .map((key, aggregate)->{
                    AggregatedReading reading = aggregate.toAggregatedReading(
                            key.window().startTime(),
                            key.window().endTime()
                    );
                    analyticService.processAggregatedReading(reading);
                    return KeyValue.pair(key.key(), reading);
                }).to(OUTPUT_TOPIC);
    }
}
