package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {
    List<Notice> findAllByOrderByCreatedAtDesc();
    List<Notice> findByPublisherId(String publisherId);
}
