package dse.controller.model;

public record CommandResult(String command, int exitCode, String output) {
    public boolean success() {
        return exitCode == 0;
    }
}
