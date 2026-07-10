package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "activity_type")
    private String activityType;

    private String details;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
