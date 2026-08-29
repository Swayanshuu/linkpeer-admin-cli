package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.Broadcast;
import com.linkpeer.admin.domain.Notice;
import com.linkpeer.admin.domain.Post;
import com.linkpeer.admin.domain.Subscription;
import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.repository.BroadcastRepository;
import com.linkpeer.admin.repository.NoticeRepository;
import com.linkpeer.admin.repository.PostRepository;
import com.linkpeer.admin.repository.SubscriptionRepository;
import com.linkpeer.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ExportService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NoticeRepository noticeRepository;
    private final BroadcastRepository broadcastRepository;
    private final ActionLogger actionLogger;

    public ExportService(UserRepository userRepository,
                         PostRepository postRepository,
                         SubscriptionRepository subscriptionRepository,
                         NoticeRepository noticeRepository,
                         BroadcastRepository broadcastRepository,
                         ActionLogger actionLogger) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.noticeRepository = noticeRepository;
        this.broadcastRepository = broadcastRepository;
        this.actionLogger = actionLogger;
    }

    public boolean exportUsers(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,name,email,user_type,role,department,college,graduating_year,is_verified,faculty_verified,subscription_plan,subscription_status,ranking_score,created_at");
            List<User> users = userRepository.findAll();
            for (User u : users) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        u.getId(), escapeCSV(u.getName()), escapeCSV(u.getEmail()),
                        u.getUserType(), u.getRole(), escapeCSV(u.getDepartment()), escapeCSV(u.getCollege()),
                        u.getGraduatingYear(), u.getIsVerified(), u.getFacultyVerified(),
                        u.getSubscriptionPlan(), u.getSubscriptionStatus(), u.getRankingScore(), u.getCreatedAt());
            }
            actionLogger.logAction("EXPORT", "USERS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportPosts(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,user_id,user_name,post_type,title,created_at,department,branch");
            List<Post> posts = postRepository.findAll();
            for (Post p : posts) {
                String authorId = p.getAuthor() != null ? p.getAuthor().getId() : null;
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                        p.getId(), authorId, escapeCSV(p.getUserName()), p.getPostType(),
                        escapeCSV(p.getTitle()), p.getCreatedAt(), escapeCSV(p.getDepartment()), escapeCSV(p.getBranch()));
            }
            actionLogger.logAction("EXPORT", "POSTS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportSubscriptions(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,user_id,plan_type,amount,status,transaction_id,start_date,end_date,created_at");
            List<Subscription> subs = subscriptionRepository.findAll();
            for (Subscription s : subs) {
                String userId = s.getUser() != null ? s.getUser().getId() : null;
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        s.getId(), userId, s.getPlanType(), s.getAmount(), s.getStatus(),
                        s.getTransactionId(), s.getStartDate(), s.getEndDate(), s.getCreatedAt());
            }
            actionLogger.logAction("EXPORT", "SUBSCRIPTIONS to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportNotices(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,title,category,publisher_id,is_important,created_at");
            List<Notice> notices = noticeRepository.findAll();
            for (Notice n : notices) {
                String pubId = n.getPublisher() != null ? n.getPublisher().getId() : null;
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        n.getId(), escapeCSV(n.getTitle()), n.getCategory(), pubId, n.getIsImportant(), n.getCreatedAt());
            }
            actionLogger.logAction("EXPORT", "NOTICES to " + filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportBroadcasts(String filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            writer.println("id,title,audience,created_by,total_recipients,total_opens,click_count,created_at");
            List<Broadcast> broadcasts = broadcastRepository.findAll();
            for (Broadcast b : broadcasts) {
                writer.printf("%s,%s,%s,%s,%d,%d,%d,%s%n",
                        b.getId(), escapeCSV(b.getTitle()), b.getAudience(), b.getCreatedBy(),
                        b.getTotalRecipients(), b.getTotalOpens(), b.getClickCount(), b.getCreatedAt());
            }
            actionLogger.logAction("EXPORT", "BROADCASTS to " + filePath);
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
