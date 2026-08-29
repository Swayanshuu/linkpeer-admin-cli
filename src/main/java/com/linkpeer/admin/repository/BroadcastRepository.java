package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BroadcastRepository extends JpaRepository<Broadcast, UUID> {
    List<Broadcast> findAllByOrderByCreatedAtDesc();
}
