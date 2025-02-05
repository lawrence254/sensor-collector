# Modular Pipeline

A scalable, multi-module Spring Boot application designed to collect, process, and analyze sensor data at scale with real-time monitoring and anomaly detection capabilities.

## 🏗 Architecture

The project is structured into several modules:

- **api**: Core API definitions and interfaces
- **common [pipeline-common]**: Shared utilities and common functionality
- **ingestion**: Sensor data ingestion handlers
- **kafka**: Message broker integration for stream processing
- **monitoring**: System health and metrics monitoring
- **processing**: Data processing and analysis pipeline
- **prometheus**: Metrics collection and exporters
- **storage**: Data persistence layer with TimescaleDB integration

## 🚀 Features

- Real-time sensor data ingestion from multiple sources
- Scalable stream processing using Apache Kafka
- Time-series data storage with TimescaleDB
- Comprehensive monitoring with Prometheus and Grafana
- Anomaly detection and alerting
- Modular architecture for easy extension

## 📋 Prerequisites

- Java 11 or higher
- Docker and Docker Compose
- Maven
- TimescaleDB
- Apache Kafka
- Prometheus
- Grafana

## 🛠 Setup

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

4. Configure your database settings in `storage/src/main/resources/application.properties`

5. Start the application:
```bash
java -jar target/modular-pipeline.jar
```

## 📊 Monitoring Setup and Configuration

### Prometheus Setup

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

3. Import Dashboards:
    - Click "+ Import"
    - Upload provided JSON dashboard files from `monitoring/dashboards/`

### Available Dashboards

1. **Sensor Overview Dashboard**
    - Real-time sensor readings
    - Historical trends
    - Sensor health status
    - Path: `monitoring/dashboards/sensor-overview.json`

2. **System Metrics Dashboard**
    - JVM metrics
    - API endpoint latency
    - Error rates
    - Resource utilization
    - Path: `monitoring/dashboards/system-metrics.json`

3. **Kafka Monitoring Dashboard**
    - Consumer lag
    - Message throughput
    - Partition distribution
    - Producer metrics
    - Path: `monitoring/dashboards/kafka-metrics.json`

4. **Anomaly Detection Dashboard**
    - Real-time anomaly detection
    - Historical anomaly patterns
    - Alert history
    - Investigation tools
    - Path: `monitoring/dashboards/anomaly-detection.json`

### Alert Configuration

1. Navigate to Alerting in Grafana
2. Import alert rules from `monitoring/alert-rules/`
3. Configure notification channels:
    - Email
    - Slack
    - PagerDuty
    - Custom webhooks

### Dashboard Customization

Each dashboard can be customized:
1. Click the dashboard settings icon (⚙️)
2. Use "Add Panel" to create new visualizations
3. Edit existing panels using the panel menu
4. Save changes to your organization

### Metrics Collection

Application metrics are exposed at:
- Application metrics: `/actuator/prometheus`
- JVM metrics: `/actuator/metrics`
- Health check: `/actuator/health`


## 🏗 Project Structure

```
modular-pipeline/
├── api/                    # Core API definitions
├── common/                 # Shared utilities
├── ingestion/             # Data ingestion handlers
├── kafka/                 # Kafka integration
│   └── docker-compose.yml
├── monitoring/            # System monitoring
├── processing/           # Data processing pipeline
├── prometheus/           # Metrics collection
│   ├── docker-compose.yml
│   └── prometheus.yml
├── src/                  # Core application code
├── storage/              # Data persistence layer
│   ├── pom.xml
│   └── storage.iml
└── pom.xml              # Root Maven configuration
```

## 📈 Metrics and Dashboards

The application provides several pre-configured Grafana dashboards:
- Sensor Data Overview
- System Performance Metrics
- Anomaly Detection Dashboard
- Kafka Stream Metrics

## 🔧 Configuration

Key configuration files:
- `prometheus.yml`: Prometheus scraping and alerting rules
- `docker-compose.yml`: Container configurations
- `storage.iml`: Database connection settings

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## 📞 Support

For support and queries, please create an issue in the repository.