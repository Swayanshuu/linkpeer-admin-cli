package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, UUID> {
    List<NoticeAttachment> findByNoticeId(UUID noticeId);
    void deleteByNoticeId(UUID noticeId);
}
