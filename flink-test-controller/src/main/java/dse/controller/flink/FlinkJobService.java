package dse.controller.flink;

import dse.controller.command.CommandRunner;
import dse.controller.model.CommandResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlinkJobService {

    private final CommandRunner runner;

    public FlinkJobService(CommandRunner runner) {
        this.runner = runner;
    }

    public String submitStateMachineJob(int parallelism) {
        String command = "docker exec jobmanager ./bin/flink run -d -p " + parallelism +
                " ./examples/streaming/StateMachineExample.jar";
        CommandResult result = runner.runPowerShell(command);
        return extractJobId(result.output());
    }

    public CommandResult cancelJob(String jobId) {
        if (jobId == null || jobId.isBlank()) {
            return new CommandResult("flink cancel", 1, "Missing job id");
        }
        return runner.runPowerShell("docker exec jobmanager ./bin/flink cancel " + jobId);
    }

    public String extractJobId(String output) {
        Pattern pattern = Pattern.compile("JobID\\s+([a-fA-F0-9]+)");
        Matcher matcher = pattern.matcher(output);
        return matcher.find() ? matcher.group(1) : "";
    }
}
