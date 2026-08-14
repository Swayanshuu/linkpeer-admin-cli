package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.NoticePublisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NoticePublisherRepository extends JpaRepository<NoticePublisher, UUID> {
    Optional<NoticePublisher> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
