package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, UUID> {
    List<AnalyticsEvent> findByTargetIdAndEventType(String targetId, String eventType);
}
