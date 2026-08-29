package com.linkpeer.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ActionLogger {

    private static final Logger logger = LoggerFactory.getLogger("AdminActionLogger");
    private final AuthService authService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ActionLogger(AuthService authService) {
        this.authService = authService;
    }

    public void logAction(String action, String target) {
        AuthService.SessionInfo session = authService.whoami();
        String email = session != null ? session.getAdminEmail() : "system";
        String timestamp = LocalDateTime.now().format(formatter);
        
        logger.info("{} \n{} \n{} \n{}", timestamp, email, action, target);
    }
}
