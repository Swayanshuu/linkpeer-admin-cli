package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.Notice;
import com.linkpeer.admin.domain.NoticePublisher;
import com.linkpeer.admin.domain.User;
import com.linkpeer.admin.repository.NoticeAttachmentRepository;
import com.linkpeer.admin.repository.NoticePublisherRepository;
import com.linkpeer.admin.repository.NoticeRepository;
import com.linkpeer.admin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticePublisherRepository noticePublisherRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;
    private final UserRepository userRepository;
    private final ActionLogger actionLogger;

    public NoticeService(NoticeRepository noticeRepository,
                         NoticePublisherRepository noticePublisherRepository,
                         NoticeAttachmentRepository noticeAttachmentRepository,
                         UserRepository userRepository,
                         ActionLogger actionLogger) {
        this.noticeRepository = noticeRepository;
        this.noticePublisherRepository = noticePublisherRepository;
        this.noticeAttachmentRepository = noticeAttachmentRepository;
        this.userRepository = userRepository;
        this.actionLogger = actionLogger;
    }

    public List<Notice> listNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Notice> getNotice(UUID id) {
        return noticeRepository.findById(id);
    }

    public List<NoticePublisher> listPublishers() {
        return noticePublisherRepository.findAll();
    }

    @Transactional
    public boolean addPublisher(String userId, String createdBy) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            if (noticePublisherRepository.existsByUserId(userId)) {
                return true;
            }
            NoticePublisher publisher = new NoticePublisher();
            publisher.setUser(userOpt.get());
            publisher.setCreatedBy(createdBy);
            publisher.setIsActive(true);
            publisher.setCreatedAt(LocalDateTime.now());
            publisher.setUpdatedAt(LocalDateTime.now());
            noticePublisherRepository.save(publisher);
            actionLogger.logAction("ADD_NOTICE_PUBLISHER", userId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removePublisher(String userId) {
        Optional<NoticePublisher> pubOpt = noticePublisherRepository.findByUserId(userId);
        if (pubOpt.isPresent()) {
            noticePublisherRepository.delete(pubOpt.get());
            actionLogger.logAction("REMOVE_NOTICE_PUBLISHER", userId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteNotice(UUID noticeId) {
        if (noticeRepository.existsById(noticeId)) {
            noticeAttachmentRepository.deleteByNoticeId(noticeId);
            noticeRepository.deleteById(noticeId);
            actionLogger.logAction("DELETE_NOTICE", noticeId.toString());
            return true;
        }
        return false;
    }
}
