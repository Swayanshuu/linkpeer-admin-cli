package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_photo")
    private String userPhoto;

    @Column(name = "post_type")
    private String postType;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String link;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_type")
    private String userType;

    private String department;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String branch;
    private String designation;

    @Column(name = "image_urls", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> imageUrls;
}
