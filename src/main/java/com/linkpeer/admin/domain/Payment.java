package com.linkpeer.admin.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "transaction_id")
    private String transactionId;

    private BigDecimal amount;
    private String currency;
    private String status; // completed, pending, failed

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
