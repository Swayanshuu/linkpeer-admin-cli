package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ActionLogger actionLogger;

    public UserService(UserRepository userRepository, ActionLogger actionLogger) {
        this.userRepository = userRepository;
        this.actionLogger = actionLogger;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    @Transactional
    public boolean verifyUser(String id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsVerified(true);
            user.setFacultyVerified(true);
            user.setFacultyVerificationStatus("approved");
            user.setFacultyVerificationReviewedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            actionLogger.logAction("VERIFY_USER", id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unverifyUser(String id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsVerified(false);
            user.setFacultyVerified(false);
            user.setFacultyVerificationStatus("not_requested");
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            actionLogger.logAction("UNVERIFY_USER", id);
            return true;
        }
        return false;
    }

    public List<User> getPendingFaculty() {
        return userRepository.findPendingFaculty();
    }

    @Transactional
    public boolean rejectFaculty(String id, String reason) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFacultyVerified(false);
            user.setFacultyVerificationStatus("rejected");
            user.setFacultyVerificationRejectionReason(reason != null ? reason : "Faculty verification rejected by admin");
            user.setFacultyVerificationReviewedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            actionLogger.logAction("REJECT_FACULTY", id);
            return true;
        }
        return false;
    }
}
