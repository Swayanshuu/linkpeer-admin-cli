package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.Post;
import com.linkpeer.admin.domain.Subscription;
import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.repository.PostRepository;
import com.linkpeer.admin.repository.SubscriptionRepository;
import com.linkpeer.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ExportService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ActionLogger actionLogger;

    public ExportService(UserRepository userRepository, PostRepository postRepository,
            SubscriptionRepository subscriptionRepository, ActionLogger actionLogger) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.actionLogger = actionLogger;
    }

    public boolean exportUsers(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,name,email,user_type,department,college,graduating_year,is_verified,subscription_plan");
            List<User> users = userRepository.findAll();
            for (User u : users) {
                writer.printf("%s,%s,%s,%s,%s,%s,%d,%b,%s%n",
                        u.getId(), escapeCSV(u.getName()), escapeCSV(u.getEmail()),
                        u.getUserType(), escapeCSV(u.getDepartment()), escapeCSV(u.getCollege()),
                        u.getGraduatingYear(), u.getIsVerified(), u.getSubscriptionPlan());
            }
            actionLogger.logAction("EXPORT", "USERS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportPosts(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,author_id,post_type,title,created_at");
            List<Post> posts = postRepository.findAll();
            for (Post p : posts) {
                String authorId = p.getAuthor() != null ? p.getAuthor().getId() : null;
                writer.printf("%s,%s,%s,%s,%s%n",
                        p.getId(), authorId, p.getPostType(), escapeCSV(p.getTitle()), p.getCreatedAt());
            }
            actionLogger.logAction("EXPORT", "POSTS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportSubscriptions(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,user_id,plan_type,status,end_date");
            List<Subscription> subs = subscriptionRepository.findAll();
            for (Subscription s : subs) {
                String userId = s.getUser() != null ? s.getUser().getId() : null;
                writer.printf("%s,%s,%s,%s,%s%n",
                        s.getId(), userId, s.getPlanType(), s.getStatus(), s.getEndDate());
            }
            actionLogger.logAction("EXPORT", "SUBSCRIPTIONS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String escapeCSV(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
