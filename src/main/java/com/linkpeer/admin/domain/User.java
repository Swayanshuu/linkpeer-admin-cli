package com.linkpeer.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    
    @Column(name = "user_type")
    private String userType;
    
    private String department;
    private String college;
    
    @Column(name = "graduating_year")
    private Integer graduatingYear;
    
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    @Column(name = "subscription_plan")
    private String subscriptionPlan;
    
    @Column(name = "faculty_proof")
    private String facultyProof;
    
    private String designation;
    
    @Column(name = "ranking_score")
    private Double rankingScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
