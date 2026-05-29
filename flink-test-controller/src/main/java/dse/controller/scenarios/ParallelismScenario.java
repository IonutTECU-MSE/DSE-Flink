package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;

public class ParallelismScenario extends AbstractScenario {

    public ParallelismScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        super(flinkJobService, flinkClient, dockerService);
    }

    @Override
    public String name() {
        return "Scenario 1: Parallelism Test";
    }

    @Override
    public void run(StringBuilder report) {
        int seconds = envInt("PARALLELISM_TEST_SECONDS", 180);
        report.append("## ").append(name()).append("\n\n");
        report.append("The same official Flink streaming job, `StateMachineExample`, is executed with parallelism values from 1 to 5. ");
        report.append("This scenario evaluates how the job behaves when the workload is divided into more parallel subtasks.\n\n");
        report.append("| Parallelism | Job ID | Runtime | Final state | Source tasks | Sink tasks |\n");
        report.append("|---:|---|---:|---|---:|---:|\n");

        for (int p = 1; p <= 5; p++) {
            String jobId = flinkJobService.submitStateMachineJob(p);
            sleepSeconds(seconds);
            String beforeCancel = flinkClient.getJobDetails(jobId);
            String sourceTasks = extractRunningTasks(beforeCancel, "Source: Events Generator Source");
            String sinkTasks = extractRunningTasks(beforeCancel, "Flat Map -> Sink: Print to Std. Out");
            flinkJobService.cancelJob(jobId);
            sleepSeconds(5);
            String afterCancel = flinkClient.getJobDetails(jobId);
            String duration = flinkClient.extractJsonValue(afterCancel, "duration");
            String state = flinkClient.extractJsonValue(afterCancel, "state");
            report.append("| ").append(p).append(" | `").append(jobId).append("` | ")
                    .append(duration).append(" ms | ").append(state).append(" | ")
                    .append(sourceTasks).append(" | ").append(sinkTasks).append(" |\n");
        }
        report.append("\nObservation: increasing job parallelism validates that Flink can schedule more subtasks across the available task slots.\n\n");
    }

    private String extractRunningTasks(String json, String vertexName) {
        int idx = json.indexOf(vertexName);
        if (idx < 0) return "0";
        int subEnd = Math.min(json.length(), idx + 1000);
        String sub = json.substring(idx, subEnd);
        return flinkClient.extractJsonValue(sub, "RUNNING");
    }
}
