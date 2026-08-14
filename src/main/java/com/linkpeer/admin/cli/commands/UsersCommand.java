package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.UserService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;

@Component
@Command(name = "users", description = "User management commands")
public class UsersCommand {

    private final UserService userService;
    private final AuthService authService;

    public UsersCommand(UserService userService, AuthService authService) {
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

    @Command(name = "list", description = "List users")
    public void list() {
        if (!checkAuth()) return;
        List<User> users = userService.listUsers();
        System.out.printf("%-36s | %-20s | %-25s | %-10s | %-15s | %-10s | %-5s | %-10s | %-10s%n", 
                "ID", "Name", "Email", "Type", "Department", "Grad Year", "Verif", "Plan", "Status");
        System.out.println("-".repeat(155));
        for (User u : users) {
            System.out.printf("%-36s | %-20s | %-25s | %-10s | %-15s | %-10s | %-5s | %-10s | %-10s%n",
                    u.getId(), truncate(u.getName(), 20), truncate(u.getEmail(), 25), truncate(u.getUserType(), 10),
                    truncate(u.getDepartment(), 15), u.getGraduatingYear(), u.getIsVerified(),
                    truncate(u.getSubscriptionPlan(), 10), truncate(u.getSubscriptionStatus(), 10));
        }
    }

    @Command(name = "view", description = "View user details")
    public void view(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        Optional<User> userOpt = userService.getUser(userId);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            System.out.println("--- User Details ---");
            System.out.println("ID: " + u.getId());
            System.out.println("Name: " + u.getName());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Role: " + u.getRole());
            System.out.println("User Type: " + u.getUserType());
            System.out.println("Phone: " + u.getPhone());
            System.out.println("Department: " + u.getDepartment());
            System.out.println("Designation: " + u.getDesignation());
            System.out.println("College: " + u.getCollege());
            System.out.println("Branch: " + u.getBranch());
            System.out.println("Stream: " + u.getStream());
            System.out.println("Graduating Year: " + u.getGraduatingYear());
            System.out.println("Profile Completed: " + u.getProfileCompleted());
            System.out.println("Verified: " + u.getIsVerified());
            System.out.println("Faculty Verified: " + u.getFacultyVerified());
            System.out.println("Faculty Verification Status: " + u.getFacultyVerificationStatus());
            System.out.println("Faculty Verification Image: " + u.getFacultyVerificationImage());
            System.out.println("Faculty Verification Reason: " + u.getFacultyVerificationRejectionReason());
            System.out.println("Subscription Plan: " + u.getSubscriptionPlan());
            System.out.println("Subscription Status: " + u.getSubscriptionStatus());
            System.out.println("Subscription Expiry: " + u.getSubscriptionExpiry());
            System.out.println("Ranking Score: " + u.getRankingScore());
            System.out.println("FCM Token: " + u.getFcmToken());
            System.out.println("Github: " + u.getGithub());
            System.out.println("Link2: " + u.getLink2());
            System.out.println("Description: " + u.getDescription());
            System.out.println("Last Login: " + u.getLastLogin());
            System.out.println("Created At: " + u.getCreatedAt());
            System.out.println("Updated At: " + u.getUpdatedAt());
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    @Command(name = "search", description = "Search users")
    public void search(@Parameters(index = "0", description = "Search keyword") String keyword) {
        if (!checkAuth()) return;
        List<User> users = userService.searchUsers(keyword);
        for (User u : users) {
            System.out.printf("%s - %s (%s)%n", u.getId(), u.getName(), u.getEmail());
        }
    }

    @Command(name = "verify", description = "Verify user")
    public void verify(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (userService.verifyUser(userId)) {
            System.out.println("\u001B[32m✓ User verified successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    @Command(name = "unverify", description = "Unverify user")
    public void unverify(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (userService.unverifyUser(userId)) {
            System.out.println("\u001B[32m✓ User unverified successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    private String truncate(String val, int len) {
        if (val == null) return "";
        return val.length() > len ? val.substring(0, len - 3) + "..." : val;
    }
}
