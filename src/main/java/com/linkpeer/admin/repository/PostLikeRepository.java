package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.PostLike;
import com.linkpeer.admin.domain.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    
    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.id.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
