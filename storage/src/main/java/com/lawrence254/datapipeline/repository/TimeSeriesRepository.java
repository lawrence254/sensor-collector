package com.lawrence254.datapipeline.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawrence254.datapipeline.model.AggregatedReading;
import com.lawrence254.datapipeline.model.LocationMetaData;
import com.lawrence254.datapipeline.model.SensorReading;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TimeSeriesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    private static final String CREATE_HYPERTABLE_SQL = """
            CREATE TABLE IF NOT EXISTS sensor_readings (
                time TIMESTAMPTZ NOT NULL,
                sensor_id TEXT NOT NULL,
                 value DOUBLE PRECISION NOT NULL,
                 sensor_type TEXT NOT NULL,
                 metadata JSONB,
                 city TEXT,
                 longitude DOUBLE PRECISION,
                 latitude DOUBLE PRECISION,
            );
            
            SELECT create_hypertable('sensor_readings', 'time');
            
            CREATE INDEX IF NOT EXISTS sensor_readings_idx ON sensor_readings(sensor_id, time DESC);
            
            CREATE TABLE IF NOT EXISTS aggregated_readings (
            time TIMESTAMPTZ NOT NULL,
            window_end TIMESTAMPTZ NOT NULL,
            session_id TEXT NOT NULL,
            avg_value DOUBLE PRECISION NOT NULL,
            min_value DOUBLE PRECISION NOT NULL,
            max_value DOUBLE PRECISION NOT NULL,
            std_dev DOUBLE PRECISION NOT NULL,
            sample_count BIGINT NOT NULL,
            sensor_type TEXT NOT NULL);
            
            SELECT create_hypertable('aggregated_readings', 'time');
            """;

    private static final String INSERT_SENSOR_READING = """
            INSERT INTO sensor_readings(
            time, sensor_id, value, sensor_type,metadata,city,longitude,latitude) VALUES (?,?,?,?,?::jsonb,?,?,?);
            """;
    private static final String INSERT_AGGREGATED_READING = """
            INSERT INTO aggregated_readings(
            time,window_end,session_id,avg_value,min_value,max_value,std_dev,sample_count,sensor_type) VALUES (?,?,?,?,?,?,?,?,?);
            """;
    private static final String QUERY_SENSOR_READINGS= """
            SELECT time, sensor_id, value, sensor_type,metadata,city,longitude,latitude
            FROM sensor_readings
            WHERE sensor_id=? AND time BETWEEN ? AND ?
            ORDER BY time DESC;
            """;

    public void saveSensorReading(SensorReading sensorReading) throws JsonProcessingException {
        jdbcTemplate.update(
                INSERT_SENSOR_READING,
                Timestamp.from(sensorReading.getTimestamp()),
                sensorReading.getSensorID(),
                sensorReading.getValue(),
                sensorReading.getSensorType().toString(),
                objectMapper.writeValueAsString(sensorReading.getMetadata()),
                sensorReading.getLocationMetaData() != null ? sensorReading.getLocationMetaData().getCity():null,
                sensorReading.getLocationMetaData() != null ? sensorReading.getLocationMetaData().getLongitude():null,
                sensorReading.getLocationMetaData() != null ? sensorReading.getLocationMetaData().getLatitude():null
        );
    }

    public void saveAggregatedReading(AggregatedReading aggregatedReading) {
        jdbcTemplate.update(
                INSERT_AGGREGATED_READING,
                Timestamp.from(aggregatedReading.getWindowStart()),
                Timestamp.from(aggregatedReading.getWindowEnd()),
                aggregatedReading.getSessionId(),
                aggregatedReading.getAvgValue(),
                aggregatedReading.getMinValue(),
                aggregatedReading.getMaxValue(),
                aggregatedReading.getStdDev(),
                aggregatedReading.getSampleCount(),
                aggregatedReading.getSensorType().toString()
        );
    }
    public List<SensorReading>getSensorReadings(String sensorId, Instant start, Instant end) throws JsonProcessingException {
        return jdbcTemplate.query(
                QUERY_SENSOR_READINGS,
                new Object[]{sensorId, Timestamp.from(start), Timestamp.from(end)},
                sensorReadingsRowMapper()
        );
    }

    private RowMapper<SensorReading> sensorReadingsRowMapper() {
        return (rs, rowNum) -> {
            SensorReading reading = new SensorReading();
            reading.setSensorID(rs.getString("sensor_id"));
            reading.setTimestamp(rs.getTimestamp("time").toInstant());
            reading.setValue(rs.getDouble("value"));
            reading.setSensorType(SensorReading.SensorType.valueOf(rs.getString("sensor_type")));

            String metadataJson = rs.getString("metadata");
            if (metadataJson != null) {
                try {
                    reading.setMetadata(
                            objectMapper.readValue(
                                    metadataJson,
                                    objectMapper.getTypeFactory().constructMapType(
                                            Map.class, String.class, String.class
                                    )
                            )
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            if (rs.getString("city") != null) {
                LocationMetaData locationMetaData = new LocationMetaData();
                locationMetaData.setCity(rs.getString("city"));
                locationMetaData.setLongitude(rs.getDouble("longitude"));
                locationMetaData.setLatitude(rs.getDouble("latitude"));
                reading.setLocationMetaData(locationMetaData);
            }
            return reading;
        };
    }

    public void setupContinuousAggregation(){
        jdbcTemplate.execute("""
            CREATE MATERIALIZED VIEW IF NOT EXISTS sensor_readings_hourly with (timescaledb.continuous) AS
                   SELECT time_bucket('1 hour', time) AS bucket,
                          sensor_id,
                          sensor_type,
                          AVG(value) as avg_value,
                          MIN(value) as min_value,
                          Max(value) as max_value,
                          COUNT(*) as sample_count,
                          FROM sensor_readings
                   GROUP BY bucket,sensor_id, sensor_type
                   WITH NO DATA;

            SELECT add_continuous_aggregate_policy('sensor_readings_hourly',
                   start_offset => INTERVAL '1 day',
                   end_offset => INTERVAL '1 hour',
                   schedule_interval => INTERVAL '1 hour');
""");
    }
}
