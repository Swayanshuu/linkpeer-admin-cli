package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}
