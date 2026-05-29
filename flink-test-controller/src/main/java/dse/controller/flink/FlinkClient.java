package dse.controller.flink;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlinkClient {

    private final String dashboardUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public FlinkClient(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String get(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dashboardUrl + path))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public String getJobDetails(String jobId) {
        return get("/jobs/" + jobId);
    }

    public String getCheckpoints(String jobId) {
        return get("/jobs/" + jobId + "/checkpoints");
    }

    public String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"?([^\\\",}]+)\\\"?");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    public String extractCheckpointCount(String checkpointsJson, String key) {
        Pattern pattern = Pattern.compile("\\\"counts\\\"\\s*:\\s*\\{[^}]*\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(checkpointsJson);
        return matcher.find() ? matcher.group(1) : "0";
    }

    public String extractVertexMetric(String jobJson, String metricName) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(metricName) + "\\\"\\s*:\\s*([0-9.]+)");
        Matcher matcher = pattern.matcher(jobJson);
        return matcher.find() ? matcher.group(1) : "0";
    }
}
