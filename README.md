# Houston

Hybrid Trading & Market Data Pipeline (Home ⇄ Cloud Architecture).

## Architecture Overview

Houston is a high-performance streaming trading platform designed for real-time market data ingestion, futures stitching, heavy mathematical analytics (Hurst cycles, wave analysis, Byers scoring), and state lifecycle tracking (`Fairways`).

### Monorepo Modules

* `ingestion/` — Raw market data ingestion from exchanges (starting with MOEX) and event publishing to the broker.
* `processing/` — Futures stitching ("по панаме") and historical/streaming database recording.
* `analytics/` — Heavy analytical models, scoring, and `Fairways` state lifecycle management (written in Scala).
* `control/` — Control Center (ЦУП): exchange schedules, metric monitoring, and debug time-travel replay mode.
* `console/` — Frontend UI for the Control Center.
* `uplink/` — Home gateway streaming real-time data securely via gRPC to the cloud.
* `common/` — Shared contracts, domain models, and enums.

## Technology Stack

* **Core Services:** Quarkus + Java 21 (Virtual Threads) for high-throughput I/O; Scala for analytics.
* **Infrastructure:** 
  * Kafka / Native Kafka (Event-driven streaming for candles and system events)
  * Redis (Caching, active ticker lists, latest prices)
  * TimescaleDB / PostgreSQL (Unified storage for time-series history and Fairways lifecycles)
* **Hybrid Cloud/Home Sync:** Native TimescaleDB replication from the home master database to the cloud replica via Tailscale. Real-time streaming via gRPC.

## Getting Started

1. Set up the environment using Docker Compose:
   ```bash
   docker compose -f dev_env_compose.yml up -d
   ```
2. Build and run modules using Maven wrapper inside respective directories.
