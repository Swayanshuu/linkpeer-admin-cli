package com.linkpeer.admin.service;

import com.linkpeer.admin.repository.PaymentRepository;
import com.linkpeer.admin.repository.PostCommentRepository;
import com.linkpeer.admin.repository.PostRepository;
import com.linkpeer.admin.repository.SubscriptionRepository;
import com.linkpeer.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AnalyticsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AnalyticsService(UserRepository userRepository, PostRepository postRepository,
                            PostCommentRepository postCommentRepository, PaymentRepository paymentRepository,
                            SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public DashboardData getDashboardData() {
        DashboardData data = new DashboardData();
        data.totalUsers = userRepository.count();
        data.students = userRepository.countByUserType("student");
        data.alumni = userRepository.countByUserType("alumni");
        data.faculty = userRepository.countByUserType("faculty");
        data.verifiedFaculty = userRepository.countVerifiedFaculty();
        data.activeSubscriptions = subscriptionRepository.countByStatusIgnoreCase("active");
        data.totalPosts = postRepository.count();
        data.totalComments = postCommentRepository.count();
        data.paymentsThisMonth = paymentRepository.countPaymentsThisMonth();
        BigDecimal revenue = paymentRepository.calculateTotalRevenue();
        data.totalRevenue = revenue != null ? revenue : BigDecimal.ZERO;
        data.newUsersToday = userRepository.countNewUsersToday();
        return data;
    }

    public static class DashboardData {
        public long totalUsers;
        public long students;
        public long alumni;
        public long faculty;
        public long verifiedFaculty;
        public long activeSubscriptions;
        public long totalPosts;
        public long totalComments;
        public long paymentsThisMonth;
        public BigDecimal totalRevenue;
        public long newUsersToday;
    }
}
