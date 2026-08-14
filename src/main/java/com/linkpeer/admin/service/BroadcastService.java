package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.Broadcast;
import com.linkpeer.admin.repository.BroadcastRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BroadcastService {

    private final BroadcastRepository broadcastRepository;
    private final ActionLogger actionLogger;

    public BroadcastService(BroadcastRepository broadcastRepository, ActionLogger actionLogger) {
        this.broadcastRepository = broadcastRepository;
        this.actionLogger = actionLogger;
    }

    public List<Broadcast> listBroadcasts() {
        return broadcastRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Broadcast> getBroadcast(UUID id) {
        return broadcastRepository.findById(id);
    }

    @Transactional
    public Broadcast createBroadcast(String title, String message, String audience, String createdBy, String imageUrl, String linkUrl) {
        Broadcast broadcast = new Broadcast();
        broadcast.setTitle(title);
        broadcast.setMessage(message);
        broadcast.setAudience(audience != null ? audience : "all");
        broadcast.setCreatedBy(createdBy != null ? createdBy : "admin");
        broadcast.setImageUrl(imageUrl);
        broadcast.setLinkUrl(linkUrl);
        broadcast.setCreatedAt(LocalDateTime.now());
        broadcast.setTotalRecipients(0);
        broadcast.setTotalOpens(0);
        broadcast.setClickCount(0);
        broadcast.setLinkClicks(0);
        
        Broadcast saved = broadcastRepository.save(broadcast);
        actionLogger.logAction("CREATE_BROADCAST", saved.getId().toString());
        return saved;
    }
}
