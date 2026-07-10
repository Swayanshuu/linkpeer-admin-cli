package com.linkpeer.admin.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            String dir = System.getProperty("user.dir");
            System.out.println("Loading .env from: " + dir);
            Dotenv dotenv = Dotenv.configure().directory(dir).ignoreIfMissing().load();
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));
            
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", envMap));
        } catch (Exception e) {
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
    }
}
