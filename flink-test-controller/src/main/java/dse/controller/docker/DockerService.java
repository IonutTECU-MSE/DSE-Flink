package dse.controller.docker;

import dse.controller.command.CommandRunner;
import dse.controller.model.CommandResult;

public class DockerService {

    private final CommandRunner runner;
    private final String projectPath;

    public DockerService(CommandRunner runner, String projectPath) {
        this.runner = runner;
        this.projectPath = projectPath;
    }

    public CommandResult startCluster() {
        return runner.runPowerShell("cd " + projectPath + "; docker compose up -d");
    }

    public CommandResult stopCluster() {
        return runner.runPowerShell("cd " + projectPath + "; docker compose down");
    }

    public CommandResult listRunningContainers() {
        return runner.runPowerShell("docker ps --format 'table {{.Names}}\t{{.Status}}\t{{.Ports}}'");
    }

    public CommandResult listAllContainers() {
        return runner.runPowerShell("docker ps -a --format 'table {{.Names}}\t{{.Status}}\t{{.Ports}}'");
    }

    public CommandResult stats() {
        return runner.runPowerShell("docker stats --no-stream");
    }

    public CommandResult stopTaskManager(String name) {
        return runner.runPowerShell("docker stop " + name);
    }

    public CommandResult startTaskManager(String name) {
        return runner.runPowerShell("docker start " + name);
    }
}
