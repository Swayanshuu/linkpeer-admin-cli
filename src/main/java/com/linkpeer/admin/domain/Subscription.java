package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "plan_type")
    private String planType;

    private String status; // active, cancelled, expired

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
