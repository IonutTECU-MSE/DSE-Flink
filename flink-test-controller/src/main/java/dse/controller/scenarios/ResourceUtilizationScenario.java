package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;
import dse.controller.model.CommandResult;

public class ResourceUtilizationScenario extends AbstractScenario {

    public ResourceUtilizationScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        super(flinkJobService, flinkClient, dockerService);
    }

    @Override
    public String name() {
        return "Scenario 3: Resource Utilization Test";
    }

    @Override
    public void run(StringBuilder report) {
        int seconds = envInt("RESOURCE_TEST_SECONDS", 180);
        report.append("## ").append(name()).append("\n\n");
        report.append("This scenario collects Docker container statistics while the official Flink stream is running with parallelism 5. ");
        report.append("The monitored values include CPU usage, memory usage, network I/O, block I/O, and process count.\n\n");

        String jobId = flinkJobService.submitStateMachineJob(5);
        sleepSeconds(seconds);
        CommandResult stats = dockerService.stats();
        flinkJobService.cancelJob(jobId);

        report.append("```text\n");
        report.append(stats.output());
        report.append("```\n\n");
        report.append("Observation: Docker statistics provide container-level evidence that the TaskManagers and JobManager were active during execution.\n\n");
    }
}
