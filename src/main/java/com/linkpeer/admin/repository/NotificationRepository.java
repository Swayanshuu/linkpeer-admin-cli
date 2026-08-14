package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
