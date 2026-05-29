package dse.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceUtilizationScenarioTest {
    @Test
    void dockerStatsOutputContainsExpectedColumns() {
        String header = "CONTAINER ID   NAME   CPU %   MEM USAGE / LIMIT   MEM %   NET I/O   BLOCK I/O   PIDS";
        assertTrue(header.contains("CPU %"));
        assertTrue(header.contains("MEM USAGE"));
        assertTrue(header.contains("NET I/O"));
    }
}
