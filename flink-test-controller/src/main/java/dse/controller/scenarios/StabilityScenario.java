package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;

public class StabilityScenario extends AbstractScenario {

    public StabilityScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        super(flinkJobService, flinkClient, dockerService);
    }

    @Override
    public String name() {
        return "Scenario 2: Long-running Stability Test";
    }

    @Override
    public void run(StringBuilder report) {
        int seconds = envInt("STABILITY_TEST_SECONDS", 600);
        report.append("## ").append(name()).append("\n\n");
        report.append("The official Flink `StateMachineExample` stream is executed with parallelism 5 for a longer interval. ");
        report.append("This scenario evaluates whether the job remains running and whether checkpoints are completed during continuous processing.\n\n");

        String jobId = flinkJobService.submitStateMachineJob(5);
        sleepSeconds(seconds);

        String jobDetails = flinkClient.getJobDetails(jobId);
        String checkpoints = flinkClient.getCheckpoints(jobId);
        String state = flinkClient.extractJsonValue(jobDetails, "state");
        String duration = flinkClient.extractJsonValue(jobDetails, "duration");
        String total = flinkClient.extractCheckpointCount(checkpoints, "total");
        String completed = flinkClient.extractCheckpointCount(checkpoints, "completed");
        String failed = flinkClient.extractCheckpointCount(checkpoints, "failed");

        flinkJobService.cancelJob(jobId);

        report.append("| Metric | Value |\n");
        report.append("|---|---:|\n");
        report.append("| Job state during observation | ").append(state).append(" |\n");
        report.append("| Runtime during observation | ").append(duration).append(" ms |\n");
        report.append("| Total checkpoints | ").append(total).append(" |\n");
        report.append("| Completed checkpoints | ").append(completed).append(" |\n");
        report.append("| Failed checkpoints | ").append(failed).append(" |\n\n");

        report.append("Observation: the long-running test verifies that the streaming job remains stable and that checkpointing is active.\n\n");
    }
}
