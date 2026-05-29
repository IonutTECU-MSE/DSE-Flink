package dse.controller;

import dse.controller.flink.FlinkClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StabilityScenarioTest {
    @Test
    void extractsCompletedCheckpointCount() {
        FlinkClient client = new FlinkClient("http://localhost:8081");
        String json = "{\"counts\":{\"restored\":0,\"total\":328,\"in_progress\":0,\"completed\":328,\"failed\":0}}";
        assertEquals("328", client.extractCheckpointCount(json, "completed"));
    }
}
