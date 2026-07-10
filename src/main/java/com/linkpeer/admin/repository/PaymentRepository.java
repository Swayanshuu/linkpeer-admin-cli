package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByStatusIgnoreCase(String status);
    List<Payment> findTop50ByOrderByCreatedAtDesc();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE EXTRACT(MONTH FROM p.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM p.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) AND lower(p.status) = 'completed'")
    long countPaymentsThisMonth();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE lower(p.status) = 'completed'")
    BigDecimal calculateTotalRevenue();
}
