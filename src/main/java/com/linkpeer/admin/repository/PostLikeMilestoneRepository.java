package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.PostLikeMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeMilestoneRepository extends JpaRepository<PostLikeMilestone, UUID> {
    Optional<PostLikeMilestone> findByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PostLikeMilestone plm WHERE plm.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
