# Changelog

All notable changes to this project will be documented in this file.


The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
