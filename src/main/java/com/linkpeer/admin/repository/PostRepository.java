package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(String authorId);
}
