package com.linkpeer.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linkpeer.admin.domain.AdminUser;
import com.linkpeer.admin.repository.AdminUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final File sessionFile;

    public AuthService(AdminUserRepository adminUserRepository, ObjectMapper objectMapper) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.objectMapper = objectMapper;
        
        String userHome = System.getProperty("user.home");
        File linkpeerDir = new File(userHome, ".linkpeer");
        if (!linkpeerDir.exists()) {
            linkpeerDir.mkdirs();
        }
        this.sessionFile = new File(linkpeerDir, "session.json");
    }

    public boolean login(String email, String rawPassword) {
        System.out.println("DEBUG: Attempting login for email: '" + email + "'");
        Optional<AdminUser> userOpt = adminUserRepository.findByEmail(email);
        System.out.println("DEBUG: User found in DB: " + userOpt.isPresent());
        if (userOpt.isPresent()) {
            AdminUser user = userOpt.get();
            String storedPassword = user.getPassword();
            System.out.println("DEBUG: Stored password length: " + (storedPassword != null ? storedPassword.length() : "null"));
            System.out.println("DEBUG: Provided password length: " + (rawPassword != null ? rawPassword.length() : "null"));
            
            boolean matches = false;
            // Check if it looks like a BCrypt hash
            if (storedPassword != null && storedPassword.startsWith("$2a$")) {
                System.out.println("DEBUG: Verifying as BCrypt hash");
                matches = passwordEncoder.matches(rawPassword, storedPassword);
            } else {
                // Fallback to plain text match if the user was inserted manually into the DB
                System.out.println("DEBUG: Verifying as plain text");
                matches = rawPassword != null && rawPassword.equals(storedPassword);
                if (!matches && rawPassword != null && storedPassword != null) {
                    System.out.println("DEBUG: Plain text mismatch! Stored: '" + storedPassword + "' vs Raw: '" + rawPassword + "'");
                }
            }
            
            if (matches) {
                createSession(user);
                return true;
            }
        }
        return false;
    }

    public void logout() {
        if (sessionFile.exists()) {
            sessionFile.delete();
        }
    }

    public SessionInfo whoami() {
        if (!sessionFile.exists()) {
            return null;
        }
        try {
            return objectMapper.readValue(sessionFile, SessionInfo.class);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isAuthenticated() {
        return whoami() != null;
    }

    private void createSession(AdminUser user) {
        try {
            ObjectNode session = objectMapper.createObjectNode();
            session.put("adminId", user.getId().toString());
            session.put("adminEmail", user.getEmail());
            session.put("loginTimestamp", LocalDateTime.now().toString());
            objectMapper.writeValue(sessionFile, session);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save session", e);
        }
    }

    public static class SessionInfo {
        private UUID adminId;
        private String adminEmail;
        private String loginTimestamp;

        public UUID getAdminId() { return adminId; }
        public void setAdminId(UUID adminId) { this.adminId = adminId; }
        public String getAdminEmail() { return adminEmail; }
        public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
        public String getLoginTimestamp() { return loginTimestamp; }
        public void setLoginTimestamp(String loginTimestamp) { this.loginTimestamp = loginTimestamp; }
    }
}
