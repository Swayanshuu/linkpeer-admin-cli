package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Subscription;
import com.linkpeer.admin.repository.SubscriptionRepository;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.SubscriptionService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.UUID;

@Component
@Command(name = "subs", description = "Subscription management commands")
public class SubsCommand {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final AuthService authService;

    public SubsCommand(SubscriptionRepository subscriptionRepository, SubscriptionService subscriptionService, AuthService authService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionService = subscriptionService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "active", description = "List active subscriptions")
    public void active() {
        if (!checkAuth()) return;
        List<Subscription> subs = subscriptionRepository.findByStatusIgnoreCase("active");
        System.out.printf("%-36s | %-20s | %-10s | %-10s | %-20s%n", 
                "User", "User Name", "Plan", "Status", "End Date");
        System.out.println("-".repeat(110));
        for (Subscription s : subs) {
            String userName = s.getUser() != null ? s.getUser().getName() : "Unknown";
            System.out.printf("%-36s | %-20s | %-10s | %-10s | %-20s%n",
                    s.getUser() != null ? s.getUser().getId() : "null",
                    userName, s.getPlanType(), s.getStatus(), s.getEndDate());
        }
    }

    @Command(name = "user", description = "View user subscriptions")
    public void user(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<Subscription> subs = subscriptionRepository.findByUserId(userId);
        for (Subscription s : subs) {
            System.out.printf("Plan: %s | Status: %s | End Date: %s%n", s.getPlanType(), s.getStatus(), s.getEndDate());
        }
    }

    @Command(name = "cancel", description = "Cancel user subscription")
    public void cancel(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        if (subscriptionService.cancelSubscription(userId)) {
            System.out.println("\u001B[32m✓ Subscription cancelled\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ No active subscription found for user\u001B[0m");
        }
    }

    @Command(name = "extend", description = "Extend user subscription")
    public void extend(@Parameters(index = "0", description = "User ID") String userId,
                       @Parameters(index = "1", description = "Days to extend") int days) {
        if (!checkAuth()) return;
        if (subscriptionService.extendSubscription(userId, days)) {
            System.out.println("\u001B[32m✓ Subscription extended\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ No active subscription found for user\u001B[0m");
        }
    }
}
