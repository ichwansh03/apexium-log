# Monitoring Apexium.log

> **Full documentation:** [docs/observability.md](./docs/observability.md).
> This page is a quick reference only.

The stack: **Micrometer + Spring Boot Actuator → Prometheus → Grafana**, plus **JSON logs → Promtail → Loki** and **Alertmanager** for alerts. All services are defined in [`docker-compose.yml`](./docker-compose.yml) and start with `docker compose up`.

## Endpoints

- **Health**: `http://localhost:8080/actuator/health` (public)
- **Info**: `http://localhost:8080/actuator/info` (authenticated)
- **Metrics**: `http://localhost:8080/actuator/metrics` (authenticated)
- **Prometheus scrape**: `http://localhost:8080/actuator/prometheus` (public, required for scraping)

## Services

- **Prometheus**: `http://localhost:9090` — scrapes `app:8080`, 15s interval, 15-day retention
- **Grafana**: `http://localhost:3000` — `admin` / `admin` by default (`GRAFANA_ADMIN_USER`, `GRAFANA_ADMIN_PASSWORD` in `.env`)
- **Alertmanager**: `http://localhost:9093`
- **Loki**: `http://localhost:3100` — 7-day retention
- **Promtail**: tails Docker container logs and pushes to Loki

## Provisioned dashboard

`Apexium.log Overview` (folder **Apexium**) is loaded automatically on Grafana startup from [`observability/grafana/dashboards/apexium-overview.json`](./observability/grafana/dashboards/apexium-overview.json):

- scrape target health, 5xx error rate
- HTTP request rate and latency (p50/p95/p99)
- JVM heap usage, CPU, GC pause time
- application logs panel (Loki)

## Prometheus configuration

Scrape config lives in [`observability/prometheus/prometheus.yml`](./observability/prometheus/prometheus.yml):

```yaml
scrape_configs:
  - job_name: 'apexium-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']   # use host.docker.internal:8080 if the app runs outside Docker
        labels:
          service: apexium-log
          environment: docker
```

To scrape from an external Prometheus instead, keep the same `metrics_path` and point it at the app's host/port. `/actuator/prometheus` must remain public in `SecurityConfig`.

## Alert rules

[`observability/prometheus/alerts.yml`](./observability/prometheus/alerts.yml):

| Alert | Condition | Severity |
|---|---|---|
| `ApexiumAppDown` | scrape target down 1m | critical |
| `ApexiumHighErrorRate` | 5xx ratio > 5% for 5m | warning |
| `ApexiumHighLatencyP95` | p95 > 2s for 5m | warning |
| `ApexiumHighHeapUsage` | heap > 90% for 10m | warning |

Routing: [`observability/alertmanager/alertmanager.yml`](./observability/alertmanager/alertmanager.yml). No notifier is configured by default — add Slack/webhook/email there and restart Alertmanager.

## Logs

Container logs are JSON (Spring `docker` profile). Query in Grafana Explore:

```logql
{service="app"} | json | level="ERROR"
{service="app"} | json | requestId="<X-Request-ID>"
```

## Key metrics

- `http_server_requests_seconds_count` — throughput by URI/status
- `http_server_requests_seconds_bucket` — latency histogram
- `jvm_memory_used_bytes` — memory usage
- `jvm_gc_pause_seconds_sum` — GC pause time
- `process_cpu_usage` — CPU usage
- `hikaricp_connections_active` — DB pool saturation
