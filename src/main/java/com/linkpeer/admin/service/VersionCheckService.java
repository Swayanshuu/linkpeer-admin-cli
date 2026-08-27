package com.linkpeer.admin.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VersionCheckService {

    private final String currentVersion;
    private static final String NPM_LATEST_URL = "https://registry.npmjs.org/@linkpeer/admin/latest";

    public VersionCheckService() {
        String ver = "1.0.7";
        try (InputStream is = getClass().getResourceAsStream("/version.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                ver = props.getProperty("cli.version", ver);
            }
        } catch (Exception ignored) {}
        this.currentVersion = ver;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Non-blocking check against NPM registry to see if a newer version exists.
     * Returns a formatted update notice if newer, or null if up-to-date or offline.
     */
    public CompletableFuture<String> checkForUpdatesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofMillis(1200))
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(NPM_LATEST_URL))
                        .timeout(Duration.ofMillis(1200))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200 && response.body() != null) {
                    Pattern pattern = Pattern.compile("\"version\"\\s*:\\s*\"([^\"]+)\"");
                    Matcher matcher = pattern.matcher(response.body());
                    if (matcher.find()) {
                        String latestVersion = matcher.group(1);
                        if (isNewerVersion(currentVersion, latestVersion)) {
                            return formatUpdateNotice(currentVersion, latestVersion);
                        }
                    }
                }
            } catch (Exception ignored) {
                // Silently swallow network timeouts / offline mode
            }
            return null;
        });
    }

    public static boolean isNewerVersion(String current, String latest) {
        if (current == null || latest == null) return false;
        String[] cParts = current.split("\\.");
        String[] lParts = latest.split("\\.");
        int length = Math.max(cParts.length, lParts.length);
        for (int i = 0; i < length; i++) {
            int cVal = i < cParts.length ? parseNum(cParts[i]) : 0;
            int lVal = i < lParts.length ? parseNum(lParts[i]) : 0;
            if (lVal > cVal) return true;
            if (lVal < cVal) return false;
        }
        return false;
    }

    private static int parseNum(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatUpdateNotice(String current, String latest) {
        String yellowBold = "\u001B[1;33m";
        String greenBold = "\u001B[1;32m";
        String reset = "\u001B[0m";

        return yellowBold + "💡 A new version of LinkPeer Admin CLI is available: " 
                + current + " -> " + greenBold + latest + yellowBold + "\n"
                + "   Run 'npm install -g @linkpeer/admin' to update." + reset + "\n";
    }
}
