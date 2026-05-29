# DSE-Flink

Apache Flink distributed cluster and automated Java benchmark controller for the Distributed Systems Engineering project.

## What is included

| Path | Description |
|---|---|
| `docker-compose.yml` | Docker Compose configuration for 1 JobManager and 5 TaskManagers |
| `flink-test-controller/` | Java application that controls Docker/Flink tests and generates a report |


## Cluster architecture

- Apache Flink 1.18
- Docker Desktop on Windows
- 1 JobManager
- 5 TaskManagers
- 2 task slots per TaskManager
- Docker network: `dse-flink_default`
- Flink Dashboard: `http://localhost:8081`

## Automated test scenarios

The Java controller covers five scenarios:

1. Parallelism test
2. Long-running stability test
3. Resource utilization test
4. Backpressure observation test
5. Fault tolerance test
