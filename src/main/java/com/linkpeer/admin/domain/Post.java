package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "post_type")
    private String postType;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String link;
    
    @Column(name = "image_urls")
    private String imageUrls;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
