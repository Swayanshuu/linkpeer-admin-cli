package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.UserService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.UUID;

@Component
@Command(name = "faculty", description = "Faculty management commands")
public class FacultyCommand {

    private final UserService userService;
    private final AuthService authService;

    public FacultyCommand(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "pending", description = "Show faculty verification queue")
    public void pending() {
        if (!checkAuth()) return;
        List<User> pending = userService.getPendingFaculty();
        if (pending.isEmpty()) {
            System.out.println("No pending faculty approvals.");
            return;
        }
        System.out.printf("%-36s | %-20s | %-15s | %-15s | %-20s%n", 
                "User ID", "Name", "Designation", "Department", "Proof");
        System.out.println("-".repeat(115));
        for (User u : pending) {
            System.out.printf("%-36s | %-20s | %-15s | %-15s | %-20s%n",
                    u.getId(), u.getName(), u.getDesignation(), u.getDepartment(), u.getFacultyProof());
        }
    }

    @Command(name = "approve", description = "Approve faculty")
    public void approve(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (userService.verifyUser(userId)) {
            System.out.println("\u001B[32m✓ Faculty approved successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    @Command(name = "reject", description = "Reject faculty")
    public void reject(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (userService.rejectFaculty(userId)) {
            System.out.println("\u001B[32m✓ Faculty rejected successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }
}
