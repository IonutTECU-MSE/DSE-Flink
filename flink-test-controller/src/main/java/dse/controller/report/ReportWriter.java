package dse.controller.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {

    private final String outputPath;

    public ReportWriter(String outputPath) {
        this.outputPath = outputPath;
    }

    public void save(String content) {
        try {
            File file = new File(outputPath);
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write report: " + e.getMessage(), e);
        }
    }
}
