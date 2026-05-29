package dse.controller;

import dse.controller.flink.FlinkJobService;
import dse.controller.command.CommandRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParallelismScenarioTest {
    @Test
    void extractsJobIdFromFlinkSubmissionOutput() {
        FlinkJobService service = new FlinkJobService(new CommandRunner(false));
        String output = "Job has been submitted with JobID abc123def456";
        assertEquals("abc123def456", service.extractJobId(output));
    }
}
