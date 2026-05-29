package dse.controller;

import dse.controller.command.CommandRunner;
import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;
import dse.controller.model.CommandResult;
import dse.controller.report.ReportWriter;
import dse.controller.scenarios.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    private static final String PROJECT_PATH = "C:\\DSE-Flink";
    private static final String DASHBOARD_URL = "http://localhost:8081";

    public static void main(String[] args) {
        System.out.println("=== DSE Flink Automated Test Controller ===");

        CommandRunner runner = new CommandRunner(true);
        DockerService dockerService = new DockerService(runner, PROJECT_PATH);
        FlinkClient flinkClient = new FlinkClient(DASHBOARD_URL);
        FlinkJobService flinkJobService = new FlinkJobService(runner);

        StringBuilder report = new StringBuilder();
        writeHeader(report);

        startCluster(report, dockerService);
        writeContainerStatus(report, dockerService);

        List<Scenario> scenarios = List.of(
                new ParallelismScenario(flinkJobService, flinkClient, dockerService),
                new StabilityScenario(flinkJobService, flinkClient, dockerService),
                new ResourceUtilizationScenario(flinkJobService, flinkClient, dockerService),
                new BackpressureScenario(flinkJobService, flinkClient, dockerService),
                new FaultToleranceScenario(flinkJobService, flinkClient, dockerService)
        );

        for (Scenario scenario : scenarios) {
            System.out.println("\nRunning " + scenario.name());
            try {
                scenario.run(report);
            } catch (Exception e) {
                report.append("## ").append(scenario.name()).append("\n\n");
                report.append("Scenario failed with error: `").append(e.getMessage()).append("`\n\n");
            }
        }

        writeFinalSummary(report);

        new ReportWriter("results/controller-report.md").save(report.toString());
        System.out.println("\nReport saved to results/controller-report.md");
        System.out.println("=== Finished ===");
    }

    private static void writeHeader(StringBuilder report) {
        report.append("# Apache Flink Automated Benchmark Report\n\n");
        report.append("Generated at: ").append(LocalDateTime.now()).append("\n\n");
        report.append("## Test Environment\n\n");
        report.append("| Item | Value |\n");
        report.append("|---|---|\n");
        report.append("| Platform | Apache Flink 1.18 |\n");
        report.append("| Deployment | Docker Desktop on Windows |\n");
        report.append("| Cluster | 1 JobManager and 5 TaskManagers |\n");
        report.append("| Task slots | 2 slots per TaskManager |\n");
        report.append("| Main streaming job | StateMachineExample |\n");
        report.append("| Flink Dashboard | ").append(DASHBOARD_URL).append(" |\n\n");
    }

    private static void startCluster(StringBuilder report, DockerService dockerService) {
        report.append("## Cluster Startup\n\n");
        CommandResult result = dockerService.startCluster();
        report.append("```text\n").append(result.output()).append("Exit code: ").append(result.exitCode()).append("\n```\n\n");
    }

    private static void writeContainerStatus(StringBuilder report, DockerService dockerService) {
        report.append("## Docker Containers\n\n");
        CommandResult result = dockerService.listRunningContainers();
        report.append("```text\n").append(result.output()).append("Exit code: ").append(result.exitCode()).append("\n```\n\n");
    }

    private static void writeFinalSummary(StringBuilder report) {
        report.append("## Final Summary\n\n");
        report.append("The automated Java controller executed five benchmark scenarios against the Docker-based Apache Flink cluster. ");
        report.append("The scenarios covered parallelism, long-running stability, resource utilization, backpressure observation, and fault tolerance. ");
        report.append("The results were collected using Docker commands and the Apache Flink REST API.\n");
    }
}
