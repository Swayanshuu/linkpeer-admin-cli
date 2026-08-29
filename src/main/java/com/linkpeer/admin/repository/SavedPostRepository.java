package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.SavedPost;
import com.linkpeer.admin.domain.SavedPostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavedPostRepository extends JpaRepository<SavedPost, SavedPostId> {
    
    @Modifying
    @Query("DELETE FROM SavedPost sp WHERE sp.id.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
