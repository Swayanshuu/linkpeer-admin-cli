package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Broadcast;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.BroadcastService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Command(name = "broadcasts", description = "Broadcast messaging commands")
public class BroadcastsCommand {

    private final BroadcastService broadcastService;
    private final AuthService authService;

    public BroadcastsCommand(BroadcastService broadcastService, AuthService authService) {
        this.broadcastService = broadcastService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "list", description = "List all broadcasts")
    public void list() {
        if (!checkAuth()) return;
        List<Broadcast> broadcasts = broadcastService.listBroadcasts();
        if (broadcasts.isEmpty()) {
            System.out.println("No broadcasts found.");
            return;
        }
        System.out.printf("%-36s | %-25s | %-10s | %-10s | %-10s | %-20s%n",
                "ID", "Title", "Audience", "Recipients", "Opens", "Created At");
        System.out.println("-".repeat(125));
        for (Broadcast b : broadcasts) {
            System.out.printf("%-36s | %-25s | %-10s | %-10d | %-10d | %-20s%n",
                    b.getId(), truncate(b.getTitle(), 25), b.getAudience(),
                    b.getTotalRecipients() != null ? b.getTotalRecipients() : 0,
                    b.getTotalOpens() != null ? b.getTotalOpens() : 0,
                    b.getCreatedAt());
        }
    }

    @Command(name = "view", description = "View broadcast details")
    public void view(@Parameters(index = "0", description = "Broadcast UUID") String broadcastIdStr) {
        if (!checkAuth()) return;
        try {
            UUID id = UUID.fromString(broadcastIdStr);
            Optional<Broadcast> opt = broadcastService.getBroadcast(id);
            if (opt.isPresent()) {
                Broadcast b = opt.get();
                System.out.println("--- Broadcast Details ---");
                System.out.println("ID: " + b.getId());
                System.out.println("Title: " + b.getTitle());
                System.out.println("Message: " + b.getMessage());
                System.out.println("Audience: " + b.getAudience());
                System.out.println("Created By: " + b.getCreatedBy());
                System.out.println("Image URL: " + b.getImageUrl());
                System.out.println("Link URL: " + b.getLinkUrl());
                System.out.println("Total Recipients: " + b.getTotalRecipients());
                System.out.println("Total Opens: " + b.getTotalOpens());
                System.out.println("Click Count: " + b.getClickCount());
                System.out.println("Link Clicks: " + b.getLinkClicks());
                System.out.println("Created At: " + b.getCreatedAt());
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }
        System.out.println("\u001B[31m✗ Broadcast not found\u001B[0m");
    }

    @Command(name = "send", description = "Send a new broadcast")
    public void send(@Parameters(index = "0", description = "Broadcast Title") String title,
                     @Parameters(index = "1", description = "Broadcast Message") String message,
                     @Option(names = {"-a", "--audience"}, defaultValue = "all", description = "Target audience (e.g. all, faculty, student)") String audience,
                     @Option(names = {"-i", "--image"}, description = "Image URL") String imageUrl,
                     @Option(names = {"-l", "--link"}, description = "Link URL") String linkUrl) {
        if (!checkAuth()) return;
        String adminEmail = authService.whoami() != null ? authService.whoami().getAdminEmail() : "admin";
        Broadcast b = broadcastService.createBroadcast(title, message, audience, adminEmail, imageUrl, linkUrl);
        System.out.println("\u001B[32m✓ Broadcast created successfully with ID: " + b.getId() + "\u001B[0m");
    }

    private String truncate(String val, int len) {
        if (val == null) return "";
        return val.length() > len ? val.substring(0, len - 3) + "..." : val;
    }
}
