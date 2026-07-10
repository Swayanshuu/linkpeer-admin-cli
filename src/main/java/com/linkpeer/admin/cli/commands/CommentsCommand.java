package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.PostComment;
import com.linkpeer.admin.repository.PostCommentRepository;
import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Command(name = "comments", description = "Comment management commands")
public class CommentsCommand {

    private final PostCommentRepository commentRepository;
    private final AuthService authService;

    public CommentsCommand(PostCommentRepository commentRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "list", description = "List all comments")
    public void list() {
        if (!checkAuth()) return;
        List<PostComment> comments = commentRepository.findAll();
        for (PostComment c : comments) {
            System.out.printf("%s | Post: %s | User: %s | %s%n",
                    c.getId(), 
                    c.getPost() != null ? c.getPost().getId() : null,
                    c.getAuthor() != null ? c.getAuthor().getName() : null,
                    c.getCreatedAt());
        }
    }

    @Command(name = "post", description = "List comments for a post")
    public void post(@Parameters(index = "0", description = "Post ID") Long postId) {
        if (!checkAuth()) return;
        List<PostComment> comments = commentRepository.findByPostId(postId);
        for (PostComment c : comments) {
            System.out.printf("%s - %s%n", c.getId(), c.getContent());
        }
    }

    @Command(name = "delete", description = "Delete comment")
    public void delete(@Parameters(index = "0", description = "Comment ID") UUID commentId) {
        if (!checkAuth()) return;
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            System.out.println("\u001B[32m✓ Comment deleted\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Comment not found\u001B[0m");
        }
    }

    @Command(name = "view", description = "View comment")
    public void view(@Parameters(index = "0", description = "Comment ID") UUID commentId) {
        if (!checkAuth()) return;
        Optional<PostComment> opt = commentRepository.findById(commentId);
        if (opt.isPresent()) {
            PostComment c = opt.get();
            System.out.println("ID: " + c.getId());
            System.out.println("Post: " + (c.getPost() != null ? c.getPost().getId() : ""));
            System.out.println("Author: " + (c.getAuthor() != null ? c.getAuthor().getName() : ""));
            System.out.println("Content: " + c.getContent());
            System.out.println("Created At: " + c.getCreatedAt());
        } else {
            System.out.println("\u001B[31m✗ Comment not found\u001B[0m");
        }
    }
}
