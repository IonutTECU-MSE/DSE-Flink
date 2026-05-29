package dse.controller;

import dse.controller.flink.FlinkClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaultToleranceScenarioTest {
    @Test
    void extractsRunningStateAfterFailureRecovery() {
        FlinkClient client = new FlinkClient("http://localhost:8081");
        String json = "{\"jid\":\"abc\",\"state\":\"RUNNING\",\"timestamps\":{\"RESTARTING\":1780072523326}}";
        assertEquals("RUNNING", client.extractJsonValue(json, "state"));
        assertEquals("1780072523326", client.extractJsonValue(json, "RESTARTING"));
    }
}
