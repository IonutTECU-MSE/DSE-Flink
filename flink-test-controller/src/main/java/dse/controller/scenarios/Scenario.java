package dse.controller.scenarios;

public interface Scenario {
    String name();
    void run(StringBuilder report);
}
