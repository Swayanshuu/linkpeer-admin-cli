package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Notice;
import com.linkpeer.admin.domain.NoticePublisher;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.NoticeService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Command(name = "notices", description = "Notice board management commands")
public class NoticesCommand {

    private final NoticeService noticeService;
    private final AuthService authService;

    public NoticesCommand(NoticeService noticeService, AuthService authService) {
        this.noticeService = noticeService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "list", description = "List all notices")
    public void list() {
        if (!checkAuth()) return;
        List<Notice> notices = noticeService.listNotices();
        if (notices.isEmpty()) {
            System.out.println("No notices found.");
            return;
        }
        System.out.printf("%-36s | %-25s | %-12s | %-10s | %-20s%n",
                "ID", "Title", "Category", "Important", "Created At");
        System.out.println("-".repeat(115));
        for (Notice n : notices) {
            System.out.printf("%-36s | %-25s | %-12s | %-10s | %-20s%n",
                    n.getId(), truncate(n.getTitle(), 25), n.getCategory(), n.getIsImportant(), n.getCreatedAt());
        }
    }

    @Command(name = "view", description = "View notice details")
    public void view(@Parameters(index = "0", description = "Notice UUID") String noticeIdStr) {
        if (!checkAuth()) return;
        try {
            UUID id = UUID.fromString(noticeIdStr);
            Optional<Notice> opt = noticeService.getNotice(id);
            if (opt.isPresent()) {
                Notice n = opt.get();
                System.out.println("--- Notice Details ---");
                System.out.println("ID: " + n.getId());
                System.out.println("Title: " + n.getTitle());
                System.out.println("Category: " + n.getCategory());
                System.out.println("Publisher: " + (n.getPublisher() != null ? n.getPublisher().getName() + " (" + n.getPublisher().getId() + ")" : "Unknown"));
                System.out.println("Is Important: " + n.getIsImportant());
                System.out.println("External URL: " + n.getExternalUrl());
                System.out.println("Content: " + n.getContent());
                System.out.println("Created At: " + n.getCreatedAt());
                System.out.println("Updated At: " + n.getUpdatedAt());
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }
        System.out.println("\u001B[31m✗ Notice not found\u001B[0m");
    }

    @Command(name = "publishers", description = "List registered notice publishers")
    public void publishers() {
        if (!checkAuth()) return;
        List<NoticePublisher> publishers = noticeService.listPublishers();
        if (publishers.isEmpty()) {
            System.out.println("No notice publishers found.");
            return;
        }
        System.out.printf("%-36s | %-20s | %-20s | %-10s%n", "User ID", "Name", "Created By", "Active");
        System.out.println("-".repeat(95));
        for (NoticePublisher p : publishers) {
            String userName = p.getUser() != null ? p.getUser().getName() : "Unknown";
            String userId = p.getUser() != null ? p.getUser().getId() : "null";
            System.out.printf("%-36s | %-20s | %-20s | %-10s%n",
                    userId, truncate(userName, 20), p.getCreatedBy(), p.getIsActive());
        }
    }

    @Command(name = "add-publisher", description = "Add a notice publisher")
    public void addPublisher(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        String adminEmail = authService.whoami() != null ? authService.whoami().getAdminEmail() : "admin";
        if (noticeService.addPublisher(userId, adminEmail)) {
            System.out.println("\u001B[32m✓ Notice publisher added successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    @Command(name = "remove-publisher", description = "Remove a notice publisher")
    public void removePublisher(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (noticeService.removePublisher(userId)) {
            System.out.println("\u001B[32m✓ Notice publisher removed successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Publisher not found\u001B[0m");
        }
    }

    @Command(name = "delete", description = "Delete notice")
    public void delete(@Parameters(index = "0", description = "Notice UUID") String noticeIdStr) {
        if (!checkAuth()) return;
        try {
            UUID id = UUID.fromString(noticeIdStr);
            if (noticeService.deleteNotice(id)) {
                System.out.println("\u001B[32m✓ Notice deleted successfully\u001B[0m");
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }
        System.out.println("\u001B[31m✗ Notice not found\u001B[0m");
    }

    private String truncate(String val, int len) {
        if (val == null) return "";
        return val.length() > len ? val.substring(0, len - 3) + "..." : val;
    }
}
