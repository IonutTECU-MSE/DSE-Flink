package dse.controller.scenarios;

import dse.controller.docker.DockerService;
import dse.controller.flink.FlinkClient;
import dse.controller.flink.FlinkJobService;

public abstract class AbstractScenario implements Scenario {

    protected final FlinkJobService flinkJobService;
    protected final FlinkClient flinkClient;
    protected final DockerService dockerService;

    protected AbstractScenario(FlinkJobService flinkJobService, FlinkClient flinkClient, DockerService dockerService) {
        this.flinkJobService = flinkJobService;
        this.flinkClient = flinkClient;
        this.dockerService = dockerService;
    }

    protected int envInt(String name, int defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    protected void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected String shorten(String value) {
        if (value == null) return "";
        int max = 2500;
        return value.length() <= max ? value : value.substring(0, max) + "\n... output truncated ...";
    }
}
