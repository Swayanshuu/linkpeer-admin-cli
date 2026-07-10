package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    void deleteByPostId(Long postId);
}
