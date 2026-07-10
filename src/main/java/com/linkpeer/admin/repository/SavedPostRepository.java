package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SavedPostRepository extends JpaRepository<SavedPost, UUID> {
    void deleteByPostId(Long postId);
}
