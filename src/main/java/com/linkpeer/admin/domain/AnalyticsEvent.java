package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "event_type")
    private String eventType;
    
    @Column(name = "target_id")
    private String targetId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
