package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;

public class BackpressureScenario extends AbstractScenario {

    public BackpressureScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        super(flinkJobService, flinkClient, dockerService);
    }

    @Override
    public String name() {
        return "Scenario 4: Backpressure Observation Test";
    }

    @Override
    public void run(StringBuilder report) {
        int seconds = envInt("BACKPRESSURE_TEST_SECONDS", 180);
        report.append("## ").append(name()).append("\n\n");
        report.append("This scenario checks the Flink job metrics while the stream is running. ");
        report.append("Backpressure indicates that downstream operators cannot process incoming records fast enough.\n\n");

        String jobId = flinkJobService.submitStateMachineJob(5);
        sleepSeconds(seconds);
        String jobDetails = flinkClient.getJobDetails(jobId);
        String state = flinkClient.extractJsonValue(jobDetails, "state");
        String backpressure = flinkClient.extractVertexMetric(jobDetails, "accumulated-backpressured-time");
        String busy = flinkClient.extractVertexMetric(jobDetails, "accumulated-busy-time");
        flinkJobService.cancelJob(jobId);

        report.append("| Metric | Value |\n");
        report.append("|---|---:|\n");
        report.append("| Job state | ").append(state).append(" |\n");
        report.append("| Accumulated backpressured time | ").append(backpressure).append(" ms |\n");
        report.append("| Accumulated busy time | ").append(busy).append(" |\n\n");
        report.append("Observation: backpressure metrics were collected through the Flink REST API while the job was running.\n\n");
    }
}
