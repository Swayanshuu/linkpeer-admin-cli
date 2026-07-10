package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByStatusIgnoreCase(String status);
    List<Subscription> findByUserId(String userId);
    
    long countByStatusIgnoreCase(String status);
}
