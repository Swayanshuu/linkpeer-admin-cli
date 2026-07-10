package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.ExportService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "export", description = "Data export commands")
public class ExportCommand {

    private final ExportService exportService;
    private final AuthService authService;

    public ExportCommand(ExportService exportService, AuthService authService) {
        this.exportService = exportService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "users", description = "Export users to CSV")
    public void users() {
        if (!checkAuth()) return;
        if (exportService.exportUsers("users.csv")) {
            System.out.println("\u001B[32m✓ Exported users successfully to users.csv\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Export failed\u001B[0m");
        }
    }

    @Command(name = "posts", description = "Export posts to CSV")
    public void posts() {
        if (!checkAuth()) return;
        if (exportService.exportPosts("posts.csv")) {
            System.out.println("\u001B[32m✓ Exported posts successfully to posts.csv\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Export failed\u001B[0m");
        }
    }

    @Command(name = "subscriptions", description = "Export subscriptions to CSV")
    public void subscriptions() {
        if (!checkAuth()) return;
        if (exportService.exportSubscriptions("subscriptions.csv")) {
            System.out.println("\u001B[32m✓ Exported subscriptions successfully to subscriptions.csv\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Export failed\u001B[0m");
        }
    }
}
