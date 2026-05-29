package dse.controller.command;

import dse.controller.model.CommandResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommandRunner {

    private final boolean printOutput;

    public CommandRunner(boolean printOutput) {
        this.printOutput = printOutput;
    }

    public CommandResult runPowerShell(String command) {
        return run(List.of("powershell.exe", "-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", command), command);
    }

    private CommandResult run(List<String> commandParts, String displayCommand) {
        StringBuilder output = new StringBuilder();
        int exitCode = -1;

        try {
            ProcessBuilder builder = new ProcessBuilder(new ArrayList<>(commandParts));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (printOutput) {
                        System.out.println(line);
                    }
                    output.append(line).append(System.lineSeparator());
                }
            }
            exitCode = process.waitFor();
        } catch (Exception e) {
            output.append("ERROR: ").append(e.getMessage()).append(System.lineSeparator());
        }

        return new CommandResult(displayCommand, exitCode, output.toString());
    }
}
