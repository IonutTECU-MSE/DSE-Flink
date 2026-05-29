# DSE-Flink

Apache Flink distributed cluster and automated Java benchmark controller for the Distributed Systems Evaluation project.

## What is included

| Path | Description |
|---|---|
| `docker-compose.yml` | Docker Compose configuration for 1 JobManager and 5 TaskManagers |
| `flink-test-controller/` | Java application that controls Docker/Flink tests and generates a report |
| `run-controller.bat` | Builds and runs the Java controller from Windows |
| `run-controller-fast.bat` | Runs a shorter demo version of the controller |
| `stop-cluster.bat` | Stops the Docker/Flink cluster |

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

## How to run

Extract this archive directly in `C:\`, so the project path becomes:

```text
C:\DSE-Flink
```

Open PowerShell or Command Prompt and run:

```bat
cd C:\DSE-Flink
run-controller.bat
```

For a shorter demo run:

```bat
run-controller-fast.bat
```

The generated report will be saved in:

```text
C:\DSE-Flink\flink-test-controller\results\controller-report.md
```

## Notes

- Docker Desktop must be running before executing the controller.
- The main Flink stream used by the controller is the official built-in `StateMachineExample`.
- The Java controller is not the Flink job itself; it controls the Docker/Flink environment and collects evidence automatically.

## Build and test the Java controller

The project includes a prebuilt controller JAR, but it can also be rebuilt using Maven through Docker:

```bat
build-controller-with-docker.bat
```

Run the Java unit tests:

```bat
run-unit-tests.bat
```

The unit tests validate the logic used by the five scenarios, such as JobID parsing, checkpoint parsing, backpressure metric extraction, resource table validation, and fault-tolerance state parsing.
