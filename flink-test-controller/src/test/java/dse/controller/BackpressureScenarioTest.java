package dse.controller;

import dse.controller.flink.FlinkClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackpressureScenarioTest {
    @Test
    void extractsAccumulatedBackpressuredTimeMetric() {
        FlinkClient client = new FlinkClient("http://localhost:8081");
        String json = "{\"metrics\":{\"accumulated-backpressured-time\":0,\"accumulated-busy-time\":12.5}}";
        assertEquals("0", client.extractVertexMetric(json, "accumulated-backpressured-time"));
    }
}
