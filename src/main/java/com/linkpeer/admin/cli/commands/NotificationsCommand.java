package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Notification;
import com.linkpeer.admin.repository.NotificationRepository;
import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;

@Component
@Command(name = "notifications", description = "Notification management commands")
public class NotificationsCommand {

    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public NotificationsCommand(NotificationRepository notificationRepository, AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "list", description = "List all recent notifications")
    public void list() {
        if (!checkAuth()) return;
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            System.out.println("No notifications found.");
            return;
        }
        System.out.printf("%-36s | %-20s | %-12s | %-25s | %-6s | %-20s%n",
                "ID", "Recipient", "Type", "Title", "Read", "Created At");
        System.out.println("-".repeat(130));
        for (Notification n : notifications) {
            String recipient = n.getUser() != null ? n.getUser().getName() : "Unknown";
            System.out.printf("%-36s | %-20s | %-12s | %-25s | %-6s | %-20s%n",
                    n.getId(), truncate(recipient, 20), n.getType(),
                    truncate(n.getTitle(), 25), n.getIsRead(), n.getCreatedAt());
        }
    }

    @Command(name = "user", description = "List notifications for a user")
    public void user(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (notifications.isEmpty()) {
            System.out.println("No notifications found for user.");
            return;
        }
        for (Notification n : notifications) {
            System.out.printf("[%s] %s: %s - %s (Read: %b)%n",
                    n.getCreatedAt(), n.getType(), n.getTitle(), n.getBody(), n.getIsRead());
        }
    }

    private String truncate(String val, int len) {
        if (val == null) return "";
        return val.length() > len ? val.substring(0, len - 3) + "..." : val;
    }
}
