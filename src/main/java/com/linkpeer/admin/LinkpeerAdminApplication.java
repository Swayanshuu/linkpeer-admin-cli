package com.linkpeer.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LinkpeerAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkpeerAdminApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
