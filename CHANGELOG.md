# Changelog

All notable changes to this project will be documented in this file.


The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Observability stack**: Prometheus metrics, Grafana dashboards (auto-provisioned), Loki log aggregation via Promtail, and Alertmanager, all wired in `docker-compose.yml`.
- Structured JSON logging for the `docker` profile, with a per-request `requestId` MDC correlation id (`X-Request-ID`).
- Prometheus alert rules: app down, high 5xx rate, high p95 latency, high JVM heap usage.
- Common Micrometer tags (`application`, `environment`) on every meter.
- Documentation: `docs/observability.md`, refreshed `MONITORING.md` and README monitoring section.

### Changed
- `/actuator/prometheus` is now publicly readable so Prometheus can scrape it; other actuator endpoints remain authenticated.
- `apexium` CLI prints Grafana/Prometheus/Loki/Alertmanager URLs and seeds `GRAFANA_ADMIN_*` defaults.

## [1.0.0] - 2026-06-04

### Added
- **Salesforce Real-time Integration**: Subscribes to Salesforce events via CometD to receive real-time log notifications.
- **Automated Log Retrieval**: Intelligent fetching of full `ApexLog` bodies using the Salesforce Tooling API.
- **Trace Management System**: Advanced scheduling for `TraceFlags` with a sliding window mechanism to bypass Salesforce's 24-hour limit.
- **Multi-Tier Storage Architecture**:
    - **PostgreSQL**: Reliable persistence for log metadata and trace job states.
    - **Redis**: High-performance caching for OAuth tokens and metadata to reduce API overhead.
    - **MinIO/S3**: Scalable object storage for raw log bodies.
- **Reactive Backend**: Modern Kotlin and Spring Boot 4.0.6 implementation with asynchronous processing.
- **Monitoring Dashboard**: React-based frontend for real-time log visualization and management of users, classes, and triggers.
- **Secure Authentication**: Environment-variable-driven OAuth2 integration.
- **Docker Support**: Ready-to-use Docker and Docker Compose configurations for the entire stack.
