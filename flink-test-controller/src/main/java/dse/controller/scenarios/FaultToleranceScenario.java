package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;
import dse.controller.model.CommandResult;

public class FaultToleranceScenario extends AbstractScenario {

    public FaultToleranceScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        super(flinkJobService, flinkClient, dockerService);
    }

    @Override
    public String name() {
        return "Scenario 5: Fault Tolerance Test";
    }

    @Override
    public void run(StringBuilder report) {
        int beforeFailure = envInt("FAULT_TEST_BEFORE_FAILURE_SECONDS", 60);
        int afterFailure = envInt("FAULT_TEST_AFTER_FAILURE_SECONDS", 120);
        report.append("## ").append(name()).append("\n\n");
        report.append("This scenario simulates worker node failure by stopping two TaskManager containers while the stream is running. ");
        report.append("The goal is to verify whether the job remains operational after losing part of the worker capacity.\n\n");

        String jobId = flinkJobService.submitStateMachineJob(5);
        sleepSeconds(beforeFailure);

        CommandResult stop4 = dockerService.stopTaskManager("taskmanager4");
        CommandResult stop5 = dockerService.stopTaskManager("taskmanager5");

        sleepSeconds(afterFailure);
        String jobDetails = flinkClient.getJobDetails(jobId);
        String state = flinkClient.extractJsonValue(jobDetails, "state");
        String restartingTimestamp = flinkClient.extractJsonValue(jobDetails, "RESTARTING");
        CommandResult containers = dockerService.listAllContainers();

        flinkJobService.cancelJob(jobId);
        CommandResult start4 = dockerService.startTaskManager("taskmanager4");
        CommandResult start5 = dockerService.startTaskManager("taskmanager5");

        report.append("### Failure injection commands\n\n");
        report.append("```text\n").append(stop4.output()).append(stop5.output()).append("```\n\n");

        report.append("### Job state after simulated failure\n\n");
        report.append("| Metric | Value |\n");
        report.append("|---|---:|\n");
        report.append("| Job state | ").append(state).append(" |\n");
        report.append("| Restarting timestamp | ").append(restartingTimestamp).append(" |\n\n");

        report.append("### Docker containers after simulated failure\n\n");
        report.append("```text\n").append(containers.output()).append("```\n\n");

        report.append("### Recovery commands\n\n");
        report.append("```text\n").append(start4.output()).append(start5.output()).append("```\n\n");

        report.append("Observation: the job state after stopping two TaskManagers indicates whether the Flink cluster remained operational with the remaining task slots.\n\n");
    }
}
