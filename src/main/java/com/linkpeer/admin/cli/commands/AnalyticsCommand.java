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
        System.out.println("========== Dashboard ==========");
        System.out.println("Total Users: " + data.totalUsers);
        System.out.println("Students: " + data.students);
        System.out.println("Alumni: " + data.alumni);
        System.out.println("Faculty: " + data.faculty);
        System.out.println("Verified Faculty: " + data.verifiedFaculty);
        System.out.println("Active Subscriptions: " + data.activeSubscriptions);
        System.out.println("Total Posts: " + data.totalPosts);
        System.out.println("Total Comments: " + data.totalComments);
        System.out.println("Payments This Month: " + data.paymentsThisMonth);
        System.out.println("Total Revenue: $" + data.totalRevenue);
        System.out.println("New Users Today: " + data.newUsersToday);
        System.out.println("===============================");
    }

    @Command(name = "top-users", description = "View top users by ranking score")
    public void topUsers() {
        if (!checkAuth()) return;
        List<User> topUsers = userRepository.findTopUsers();
        for (int i = 0; i < topUsers.size(); i++) {
            User u = topUsers.get(i);
            System.out.printf("%d. %s (Score: %d)%n", (i+1), u.getName(), u.getRankingScore() != null ? u.getRankingScore() : 0);
        }
    }

    @Command(name = "activity", description = "View user activity")
    public void activity(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<UserActivity> activities = userActivityRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (activities.isEmpty()) {
            System.out.println("No activity found for user.");
        }
        for (UserActivity a : activities) {
            System.out.printf("[%s] %s%n", a.getCreatedAt(), a.getActivityType());
        }
    }

    @Command(name = "views", description = "View profile views")
    public void views(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<AnalyticsEvent> events = analyticsEventRepository.findByTargetIdAndEventType(userId, "profile_view");
        System.out.println("Total Profile Views: " + events.size());
    }
}
