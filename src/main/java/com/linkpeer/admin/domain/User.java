package com.linkpeer.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    private String name;
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    private String branch;
    private String college;
    private String stream;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "graduating_year")
    private Integer graduatingYear;

    @Column(name = "profile_completed")
    private Boolean profileCompleted;

    private String department;
    private String designation;
    private String phone;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String github;
    private String link2;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "subscription_plan")
    private String subscriptionPlan;

    @Column(name = "subscription_status")
    private String subscriptionStatus;

    @Column(name = "subscription_expiry")
    private LocalDateTime subscriptionExpiry;

    @Column(name = "ranking_score")
    private Integer rankingScore;

    @Column(name = "faculty_verified")
    private Boolean facultyVerified;

    @Column(name = "faculty_verification_image")
    private String facultyVerificationImage;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "faculty_verification_rejection_reason")
    private String facultyVerificationRejectionReason;

    @Column(name = "faculty_verification_reviewed_at")
    private LocalDateTime facultyVerificationReviewedAt;

    @Column(name = "faculty_verification_reviewed_by")
    private String facultyVerificationReviewedBy;

    @Column(name = "faculty_verification_status")
    private String facultyVerificationStatus;
}
