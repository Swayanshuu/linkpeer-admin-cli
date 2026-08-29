package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    List<UserActivity> findByUserIdOrderByCreatedAtDesc(String userId);
}
