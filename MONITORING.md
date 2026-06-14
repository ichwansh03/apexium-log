# Monitoring Apexium.log

This project uses Spring Boot Actuator and Micrometer to expose metrics for Prometheus.

## Actuator Endpoints

- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Prometheus**: `http://localhost:8080/actuator/prometheus`

## Docker Compose Integration

Prometheus and Grafana are included in the `docker-compose.yml`. When you run `docker-compose up`, these services will start automatically.

- **Prometheus**: `http://localhost:9090` (Scrapes the app automatically)
- **Grafana**: `http://localhost:3000` (User: `admin`, Password: `admin`)

## Prometheus Configuration

To scrape metrics from this application, add the following to your `prometheus.yml`:

```yaml
scrape_configs:
  - job_name: 'apexium-log'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['host.docker.internal:8080'] # Use 'localhost:8080' if running outside Docker
```

## Grafana Dashboard

You can use standard JVM dashboards to visualize the metrics. We recommend:

1.  **JVM (Micrometer)**: [Dashboard ID: 4701](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)
2.  **Spring Boot Statistics**: [Dashboard ID: 6756](https://grafana.com/grafana/dashboards/6756-spring-boot-statistics/)

### Key Metrics to Watch:

- `jvm_memory_used_bytes`: Current memory usage.
- `jvm_gc_pause_seconds_count`: Garbage collection frequency.
- `http_server_requests_seconds_count`: API request throughput.
- `process_cpu_usage`: CPU usage of the application.
