package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "broadcasts")
public class Broadcast {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(nullable = false)
    private String audience;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "total_recipients")
    private Integer totalRecipients;

    @Column(name = "total_opens")
    private Integer totalOpens;

    @Column(name = "click_count")
    private Integer clickCount;

    @Column(name = "link_clicks")
    private Integer linkClicks;
}
