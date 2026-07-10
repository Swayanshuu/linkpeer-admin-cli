package com.linkpeer.admin.service;

import com.linkpeer.admin.repository.PostRepository;
import com.linkpeer.admin.repository.PostCommentRepository;
import com.linkpeer.admin.repository.PostLikeRepository;
import com.linkpeer.admin.repository.SavedPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final SavedPostRepository savedPostRepository;
    private final ActionLogger actionLogger;

    public PostService(PostRepository postRepository,
            PostCommentRepository postCommentRepository,
            PostLikeRepository postLikeRepository,
            SavedPostRepository savedPostRepository,
            ActionLogger actionLogger) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postLikeRepository = postLikeRepository;
        this.savedPostRepository = savedPostRepository;
        this.actionLogger = actionLogger;
    }

    @Transactional
    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            savedPostRepository.deleteByPostId(postId);
            postLikeRepository.deleteByPostId(postId);
            postCommentRepository.deleteByPostId(postId);
            postRepository.deleteById(postId);
            actionLogger.logAction("DELETE_POST", "POST_" + postId);
            return true;
        }
        return false;
    }
}
