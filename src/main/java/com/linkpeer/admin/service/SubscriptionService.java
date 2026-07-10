package com.linkpeer.admin.service;

import com.linkpeer.admin.domain.Subscription;
import com.linkpeer.admin.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ActionLogger actionLogger;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, ActionLogger actionLogger) {
        this.subscriptionRepository = subscriptionRepository;
        this.actionLogger = actionLogger;
    }

    @Transactional
    public boolean cancelSubscription(String userId) {
        List<Subscription> subs = subscriptionRepository.findByUserId(userId);
        boolean cancelled = false;
        for (Subscription sub : subs) {
            if ("active".equalsIgnoreCase(sub.getStatus())) {
                sub.setStatus("cancelled");
                subscriptionRepository.save(sub);
                cancelled = true;
            }
        }
        if (cancelled) {
            actionLogger.logAction("CANCEL_SUBSCRIPTION", userId.toString());
        }
        return cancelled;
    }

    @Transactional
    public boolean extendSubscription(String userId, int days) {
        List<Subscription> subs = subscriptionRepository.findByUserId(userId);
        boolean extended = false;
        for (Subscription sub : subs) {
            if ("active".equalsIgnoreCase(sub.getStatus()) && sub.getEndDate() != null) {
                sub.setEndDate(sub.getEndDate().plusDays(days));
                subscriptionRepository.save(sub);
                extended = true;
            }
        }
        if (extended) {
            actionLogger.logAction("EXTEND_SUBSCRIPTION", userId + " days: " + days);
        }
        return extended;
    }
}
