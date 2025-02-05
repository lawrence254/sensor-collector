# Detailed Setup Instructions

## Table of Contents
- [Initial Setup](#initial-setup)
- [Database Configuration](#database-configuration)
- [Monitoring Setup](#monitoring-setup)
    - [Prometheus Configuration](#prometheus-configuration)
    - [Grafana Setup](#grafana-setup)
    - [Dashboard Configuration](#dashboard-configuration)
    - [Alert Configuration](#alert-configuration)

## Initial Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/modular-pipeline.git
cd modular-pipeline
```

2. Start the required services using Docker Compose:
```bash
cd kafka
docker-compose up -d

cd ../prometheus
docker-compose up -d
```

3. Build the project:
```bash
mvn clean install
```

4. Start the application:
```bash
java -jar target/modular-pipeline.jar
```

## Database Configuration

Configure your database settings in `storage/src/main/resources/application.properties`

## Monitoring Setup

### Prometheus Configuration

1. Verify Prometheus is running:
```bash
docker ps | grep prometheus
```

2. Access the Prometheus UI at `http://localhost:9090`

3. Verify target scraping is working:
    - Navigate to Status > Targets
    - Check that all application endpoints are in "UP" state

### Grafana Setup

1. Access Grafana at `http://localhost:3000`
    - Default credentials: admin/admin

2. Add Data Sources:
    - Click "Configuration" > "Data Sources"
    - Add Prometheus:
        - URL: `http://prometheus:9090`
        - Access: Browser
        - Click "Test & Save"
    - Add TimescaleDB:
        - Type: PostgreSQL
        - Host: `timescaledb:5432`
        - Database: Your database name
        - User & Password: Your credentials
        - SSL Mode: disable
        - Version: 12+

### Dashboard Configuration

1. **Sensor Overview Dashboard**
    - Import from `monitoring/dashboards/sensor-overview.json`
    - Shows real-time sensor readings and historical trends
    - Displays sensor health status

2. **System Metrics Dashboard**
    - Import from `monitoring/dashboards/system-metrics.json`
    - Shows JVM metrics and API endpoint latency
    - Monitors error rates and resource utilization

3. **Kafka Monitoring Dashboard**
    - Import from `monitoring/dashboards/kafka-metrics.json`
    - Tracks consumer lag and message throughput
    - Monitors partition distribution and producer metrics

4. **Anomaly Detection Dashboard**
    - Import from `monitoring/dashboards/anomaly-detection.json`
    - Shows real-time anomaly detection and historical patterns
    - Provides investigation tools and alert history

### Alert Configuration

1. Navigate to Alerting in Grafana
2. Import alert rules from `monitoring/alert-rules/`
3. Configure notification channels:
    - Email
    - Slack
    - PagerDuty
    - Custom webhooks

### Metrics Collection Endpoints

- Application metrics: `/actuator/prometheus`
- JVM metrics: `/actuator/metrics`
- Health check: `/actuator/health`

### Dashboard Customization

Each dashboard can be customized:
1. Click the dashboard settings icon (⚙️)
2. Use "Add Panel" to create new visualizations
3. Edit existing panels using the panel menu
4. Save changes to your organization