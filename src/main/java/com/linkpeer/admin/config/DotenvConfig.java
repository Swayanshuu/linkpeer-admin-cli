package com.linkpeer.admin.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> envMap = new HashMap<>();

        // 1. Try loading from user home ~/.linkpeer/.env
        String userHome = System.getProperty("user.home");
        File globalEnvDir = new File(userHome + File.separator + ".linkpeer");
        File globalEnvFile = new File(globalEnvDir, ".env");
        if (globalEnvFile.exists()) {
            try {
                Dotenv dotenvHome = Dotenv.configure().directory(globalEnvDir.getAbsolutePath()).ignoreIfMissing().load();
                dotenvHome.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));
            } catch (Exception ignored) {}
        }

        // 2. Try loading from current working directory .env (overrides global)
        String currentDir = System.getProperty("user.dir");
        try {
            Dotenv dotenvCurrent = Dotenv.configure().directory(currentDir).ignoreIfMissing().load();
            dotenvCurrent.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));
        } catch (Exception ignored) {}

        // 3. Ensure any loaded SUPABASE_DB_URL has PgBouncer-compatible query settings
        String rawUrl = (String) envMap.get("SUPABASE_DB_URL");
        if (rawUrl == null) {
            rawUrl = System.getenv("SUPABASE_DB_URL");
        }
        if (rawUrl != null && rawUrl.contains("postgresql")) {
            String sanitizedUrl = sanitizePgUrl(rawUrl);
            envMap.put("SUPABASE_DB_URL", sanitizedUrl);
            envMap.put("spring.datasource.url", sanitizedUrl);
        }

        String dbUser = (String) envMap.get("SUPABASE_DB_USERNAME");
        if (dbUser == null) dbUser = System.getenv("SUPABASE_DB_USERNAME");
        if (dbUser != null) {
            envMap.put("spring.datasource.username", dbUser);
        }

        String dbPass = (String) envMap.get("SUPABASE_DB_PASSWORD");
        if (dbPass == null) dbPass = System.getenv("SUPABASE_DB_PASSWORD");
        if (dbPass != null) {
            envMap.put("spring.datasource.password", dbPass);
        }

        if (!envMap.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", envMap));
        }

        // 4. Check if DB URL is defined
        boolean hasUrl = environment.containsProperty("SUPABASE_DB_URL") 
                || environment.containsProperty("spring.datasource.url")
                || System.getenv().containsKey("SUPABASE_DB_URL") 
                || envMap.containsKey("SUPABASE_DB_URL");
                
        if (!hasUrl) {
            System.err.println("\n❌ ERROR: Database configuration missing (SUPABASE_DB_URL not found).");
            System.err.println("   Please place a .env file in the current directory or in: " + globalEnvFile.getAbsolutePath());
            System.err.println("   Refer to .env.example for required database environment keys.\n");
            System.exit(1);
        }
    }

    private String sanitizePgUrl(String url) {
        if (!url.contains("prepareThreshold=")) {
            url += (url.contains("?") ? "&" : "?") + "prepareThreshold=0";
        }
        if (!url.contains("preferQueryMode=")) {
            url += (url.contains("?") ? "&" : "?") + "preferQueryMode=simple";
        }
        return url;
    }
}

