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

## 🛠 Quick Start

See [SETUP.md](./SETUP.md) for detailed setup and configuration instructions.

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
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── pom.xml              # Root Maven configuration
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 🛠 Setup
Setup instructions can be found here: [Setup instructions](SETUP.md)

## 📝 License

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## 📞 Support

For support and queries, please create an issue in the repository.