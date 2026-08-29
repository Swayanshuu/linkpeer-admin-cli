package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.AnalyticsEvent;
import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.domain.UserActivity;
import com.linkpeer.admin.repository.AnalyticsEventRepository;
import com.linkpeer.admin.repository.UserActivityRepository;
import com.linkpeer.admin.repository.UserRepository;
import com.linkpeer.admin.service.AnalyticsService;
import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;

@Component
@Command(name = "analytics", aliases = {"dashboard"}, description = "Analytics commands")
public class AnalyticsCommand {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AuthService authService;

    public AnalyticsCommand(AnalyticsService analyticsService, UserRepository userRepository,
                             UserActivityRepository userActivityRepository,
                             AnalyticsEventRepository analyticsEventRepository,
                             AuthService authService) {
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
        this.userActivityRepository = userActivityRepository;
        this.analyticsEventRepository = analyticsEventRepository;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "dashboard", description = "View analytics dashboard")
    public void dashboard() {
        if (!checkAuth()) return;
        AnalyticsService.DashboardData data = analyticsService.getDashboardData();
        String cyan = "\u001B[1;36m";
        String green = "\u001B[1;32m";
        String yellow = "\u001B[1;33m";
        String purple = "\u001B[1;35m";
        String bold = "\u001B[1m";
        String reset = "\u001B[0m";

        System.out.println("\n" + cyan + "════════════════════ 📊 LINKPEER DASHBOARD ════════════════════" + reset);
        System.out.printf("  %-25s : %s%d%s%n", "Total Users", bold, data.totalUsers, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Students", green, data.students, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Alumni", cyan, data.alumni, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Faculty", purple, data.faculty, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Verified Faculty", green, data.verifiedFaculty, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Active Subscriptions", yellow, data.activeSubscriptions, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Total Posts", bold, data.totalPosts, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Total Comments", bold, data.totalComments, reset);
        System.out.printf("  %-25s : %s%d%s%n", "Payments This Month", green, data.paymentsThisMonth, reset);
        System.out.printf("  %-25s : %s$%s%s%n", "Total Revenue", green + bold, data.totalRevenue, reset);
        System.out.printf("  %-25s : %s%d%s%n", "New Users Today", yellow + bold, data.newUsersToday, reset);
        System.out.println(cyan + "═════════════════════════════════════════════════════════════" + reset + "\n");
    }

    @Command(name = "top-users", description = "View top users by ranking score")
    public void topUsers() {
        if (!checkAuth()) return;
        List<User> topUsers = userRepository.findTopUsers();
        System.out.println("\n\u001B[1;36m🏆 Top Ranked Users:\u001B[0m");
        for (int i = 0; i < topUsers.size(); i++) {
            User u = topUsers.get(i);
            System.out.printf("  \u001B[1;32m%2d.\u001B[0m \u001B[1m%-25s\u001B[0m (Score: \u001B[1;33m%d\u001B[0m)%n", (i+1), u.getName(), u.getRankingScore() != null ? u.getRankingScore() : 0);
        }
        System.out.println();
    }

    @Command(name = "activity", description = "View user activity")
    public void activity(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<UserActivity> activities = userActivityRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (activities.isEmpty()) {
            System.out.println("\u001B[33mNo activity found for user.\u001B[0m");
            return;
        }
        System.out.println("\n\u001B[1;36m📋 User Activity Log:\u001B[0m");
        for (UserActivity a : activities) {
            System.out.printf("  \u001B[90m[%s]\u001B[0m \u001B[1;32m%s\u001B[0m%n", a.getCreatedAt(), a.getActivityType());
        }
        System.out.println();
    }

    @Command(name = "views", description = "View profile views")
    public void views(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<AnalyticsEvent> events = analyticsEventRepository.findByTargetIdAndEventType(userId, "profile_view");
        System.out.println("\u001B[1;36mTotal Profile Views:\u001B[0m \u001B[1;32m" + events.size() + "\u001B[0m");
    }
}
