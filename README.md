# Salesforce Observable Logging System

A real-time observability platform built with Kotlin and Spring Boot to centralize, monitor, and analyze Salesforce debug logs and system events.

## Project Goals

- **Centralized Visibility**: Aggregate logs from multiple Salesforce environments into a single backend service.
- **Real-time Monitoring**: Use Salesforce Streaming API (CometD) to react to log events the moment they are generated.
- **Deep Inspection**: Automatically fetch full `ApexLog` bodies using the Tooling API for detailed root-cause analysis.
- **Scalable Processing**: Provide a robust Kotlin-based foundation to filter, transform, and forward logs to external observability tools (e.g., ELK, Splunk, Datadog).

## Architecture Design

The system follows a reactive architecture to handle the asynchronous nature of Salesforce log generation.

### Visual Workflow
```text
+----------------+       Streaming API       +----------------+
|   Salesforce   | ------------------------> |  CometD Client |
| (TraceFlags &  |      (PushTopic)          | (Kotlin/Jetty) |
|  PushTopic)    |                           +-------+--------+
+-------^--------+                                   |
        |                                            | Log Event
        |            Tooling API / REST              v
        +------------------------------------ [ Kotlin Backend ]
               (Fetch Metadata & Body)               |
                                                     | Processed Log
                                                     v
                                             +----------------+
                                             |  Observability |
                                             |     Stack      |
                                             +----------------+
```

### Technical Flow (Mermaid)
```mermaid
graph TD
    SFDC[Salesforce] -- Streaming API / PushTopic --> CometD[CometD Client]
    CometD -- Log Event --> Ktor[Kotlin Backend]
    Ktor -- Query Metadata --> ToolingAPI[SFDC Tooling API]
    ToolingAPI -- Log ID --> Ktor
    Ktor -- Download Body --> ToolingAPI
    Ktor -- Processed Log --> Storage[External Observability Stack]
```

### Components

1.  **Salesforce (Source)**:
    *   **TraceFlags**: Configured to capture logs for specific users/classes.
    *   **PushTopic**: Broadcasts notifications when new `ApexLog` or custom log records are created.
2.  **Kotlin Integration Service**:
    *   **CometD Client**: Maintains a long-polling connection to Salesforce Streaming API.
    *   **Log Processor**: Orchestrates the fetching of log bodies and performs initial parsing.
    *   **Tooling/REST Client**: Communicates with Salesforce APIs for metadata and log retrieval.
3.  **Observability Layer (Optional)**:
    *   The processed logs can be forwarded to tools like Elasticsearch, CloudWatch, or custom dashboards.

## Tech Stack

- **Language**: Kotlin 2.2.21
- **Framework**: Spring Boot 4.0.6
- **Communication**: 
    - CometD (Bayeux Protocol) for real-time events.
    - Salesforce Tooling & REST API for data retrieval.
- **Build Tool**: Maven

## Getting Started

1.  **Salesforce Setup**: Ensure you have a `PushTopic` created and `TraceFlags` active in your Salesforce org.
2.  **Environment Configuration**: To avoid hardcoding sensitive information like `client_id` and `client_secret` in `application.properties`, create a `.env` file in the root directory. Follow this structure:
    ```bash
    # Salesforce Configuration
    SALESFORCE_INSTANCE_ORG=https://login.salesforce.com
    SALESFORCE_CLIENT_ID=your_client_id
    SALESFORCE_CLIENT_SECRET=your_client_secret
    SALESFORCE_GRANT_TYPE=password # or your preferred grant type
    SALESFORCE_API_VERSION=v60.0

    # Database Configuration
    DB_HOST=localhost
    DB_PORT=5432
    DB_NAME=sfdc_logs
    DB_USER=postgres
    DB_PASSWORD=postgres

    # Redis Configuration
    REDIS_HOST=localhost
    REDIS_PORT=6379
    ```
3.  **Run Application**: Ensure your local database (PostgreSQL) and Redis are running, then start the application using Maven.
4.  **Reference**: See [SKILL.md](./SKILL.md) for detailed implementation snippets and API references.

---
*Maintained by the Observability Engineering Team.*
