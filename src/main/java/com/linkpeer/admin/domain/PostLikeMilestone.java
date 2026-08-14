package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "post_like_milestones")
public class PostLikeMilestone {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Column(name = "reached_10")
    private Boolean reached10;

    @Column(name = "reached_30")
    private Boolean reached30;

    @Column(name = "reached_50")
    private Boolean reached50;

    @Column(name = "reached_100")
    private Boolean reached100;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
