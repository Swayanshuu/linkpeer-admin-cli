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
        String cyanBold = "\u001B[1;36m";
        String reset = "\u001B[0m";

        System.out.printf(cyanBold + "%-36s | %-20s | %-25s | %-10s | %-15s | %-10s | %-5s | %-10s | %-10s" + reset + "%n", 
                "ID", "Name", "Email", "Type", "Department", "Grad Year", "Verif", "Plan", "Status");
        System.out.println("\u001B[90m" + "-".repeat(155) + reset);
        for (User u : users) {
            String verifStatus = Boolean.TRUE.equals(u.getIsVerified()) ? "\u001B[32m✓\u001B[0m" : "\u001B[31m✗\u001B[0m";
            System.out.printf("%-36s | \u001B[1m%-20s\u001B[0m | %-25s | %-10s | %-15s | %-10s | %-5s | %-10s | %-10s%n",
                    u.getId(), truncate(u.getName(), 20), truncate(u.getEmail(), 25), truncate(u.getUserType(), 10),
                    truncate(u.getDepartment(), 15), u.getGraduatingYear(), verifStatus,
                    truncate(u.getSubscriptionPlan(), 10), truncate(u.getSubscriptionStatus(), 10));
        }
    }

    @Command(name = "view", description = "View user details")
    public void view(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        Optional<User> userOpt = userService.getUser(userId);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            String keyColor = "\u001B[1;34m";
            String reset = "\u001B[0m";
            System.out.println("\n\u001B[1;36m👤 --- User Details --- \u001B[0m");
            System.out.println(keyColor + "ID: " + reset + u.getId());
            System.out.println(keyColor + "Name: " + reset + u.getName());
            System.out.println(keyColor + "Email: " + reset + u.getEmail());
            System.out.println(keyColor + "Role: " + reset + u.getRole());
            System.out.println(keyColor + "User Type: " + reset + u.getUserType());
            System.out.println(keyColor + "Phone: " + reset + u.getPhone());
            System.out.println(keyColor + "Department: " + reset + u.getDepartment());
            System.out.println(keyColor + "Designation: " + reset + u.getDesignation());
            System.out.println(keyColor + "College: " + reset + u.getCollege());
            System.out.println(keyColor + "Branch: " + reset + u.getBranch());
            System.out.println(keyColor + "Stream: " + reset + u.getStream());
            System.out.println(keyColor + "Graduating Year: " + reset + u.getGraduatingYear());
            System.out.println(keyColor + "Profile Completed: " + reset + u.getProfileCompleted());
            System.out.println(keyColor + "Verified: " + reset + (Boolean.TRUE.equals(u.getIsVerified()) ? "\u001B[32m✓ Yes\u001B[0m" : "\u001B[31m✗ No\u001B[0m"));
            System.out.println(keyColor + "Faculty Verified: " + reset + u.getFacultyVerified());
            System.out.println(keyColor + "Faculty Verification Status: " + reset + u.getFacultyVerificationStatus());
            System.out.println(keyColor + "Faculty Verification Image: " + reset + u.getFacultyVerificationImage());
            System.out.println(keyColor + "Faculty Verification Reason: " + reset + u.getFacultyVerificationRejectionReason());
            System.out.println(keyColor + "Subscription Plan: " + reset + u.getSubscriptionPlan());
            System.out.println(keyColor + "Subscription Status: " + reset + u.getSubscriptionStatus());
            System.out.println(keyColor + "Subscription Expiry: " + reset + u.getSubscriptionExpiry());
            System.out.println(keyColor + "Ranking Score: " + reset + u.getRankingScore());
            System.out.println(keyColor + "FCM Token: " + reset + u.getFcmToken());
            System.out.println(keyColor + "Github: " + reset + u.getGithub());
            System.out.println(keyColor + "Link2: " + reset + u.getLink2());
            System.out.println(keyColor + "Description: " + reset + u.getDescription());
            System.out.println(keyColor + "Last Login: " + reset + u.getLastLogin());
            System.out.println(keyColor + "Created At: " + reset + u.getCreatedAt());
            System.out.println(keyColor + "Updated At: " + reset + u.getUpdatedAt());
            System.out.println();
        } else {
            System.out.println("\u001B[31m✗ User not found\u001B[0m");
        }
    }

    @Command(name = "search", description = "Search users")
    public void search(@Parameters(index = "0", description = "Search keyword") String keyword) {
        if (!checkAuth()) return;
        List<User> users = userService.searchUsers(keyword);
        if (users.isEmpty()) {
            System.out.println("\u001B[33mNo users found matching keyword.\u001B[0m");
            return;
        }
        System.out.println("\u001B[1;36m🔍 Search Results:\u001B[0m");
        for (User u : users) {
            System.out.printf("  \u001B[36m%s\u001B[0m - \u001B[1m%s\u001B[0m (\u001B[32m%s\u001B[0m)%n", u.getId(), u.getName(), u.getEmail());
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
