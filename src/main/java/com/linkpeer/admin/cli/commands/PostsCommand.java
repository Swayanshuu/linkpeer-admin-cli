package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Post;
import com.linkpeer.admin.repository.PostRepository;
import com.linkpeer.admin.service.AuthService;
import com.linkpeer.admin.service.PostService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;

@Component
@Command(name = "posts", description = "Post management commands")
public class PostsCommand {

    private final PostRepository postRepository;
    private final PostService postService;
    private final AuthService authService;

    public PostsCommand(PostRepository postRepository, PostService postService, AuthService authService) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "list", description = "List posts")
    public void list() {
        if (!checkAuth()) return;
        List<Post> posts = postRepository.findAll();
        System.out.printf("%-10s | %-20s | %-10s | %-25s | %-20s%n", 
                "Post ID", "Author", "Type", "Title", "Created At");
        System.out.println("-".repeat(95));
        for (Post p : posts) {
            String authorName = p.getUserName() != null ? p.getUserName() : 
                                (p.getAuthor() != null ? p.getAuthor().getName() : "Unknown");
            System.out.printf("%-10d | %-20s | %-10s | %-25s | %-20s%n",
                    p.getId(), truncate(authorName, 20), p.getPostType(), truncate(p.getTitle(), 25), p.getCreatedAt());
        }
    }

    @Command(name = "view", description = "View post details")
    public void view(@Parameters(index = "0", description = "Post ID") Long postId) {
        if (!checkAuth()) return;
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post p = postOpt.get();
            System.out.println("--- Post Details ---");
            System.out.println("ID: " + p.getId());
            System.out.println("Author ID: " + (p.getAuthor() != null ? p.getAuthor().getId() : "null"));
            System.out.println("Author Name: " + p.getUserName());
            System.out.println("Author Photo: " + p.getUserPhoto());
            System.out.println("Type: " + p.getPostType());
            System.out.println("Title: " + p.getTitle());
            System.out.println("Content: " + p.getContent());
            System.out.println("Link: " + p.getLink());
            System.out.println("Image URL: " + p.getImageUrl());
            System.out.println("Image URLs List: " + p.getImageUrls());
            System.out.println("File Name: " + p.getFileName());
            System.out.println("File URL: " + p.getFileUrl());
            System.out.println("File Type: " + p.getFileType());
            System.out.println("Department: " + p.getDepartment());
            System.out.println("Branch: " + p.getBranch());
            System.out.println("Designation: " + p.getDesignation());
            System.out.println("Created At: " + p.getCreatedAt());
            System.out.println("Updated At: " + p.getUpdatedAt());
        } else {
            System.out.println("\u001B[31m✗ Invalid post id\u001B[0m");
        }
    }

    @Command(name = "delete", description = "Delete post")
    public void delete(@Parameters(index = "0", description = "Post ID") Long postId) {
        if (!checkAuth()) return;
        if (postService.deletePost(postId)) {
            System.out.println("\u001B[32m✓ Post deleted successfully\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Invalid post id\u001B[0m");
        }
    }

    @Command(name = "user", description = "List user posts")
    public void user(@Parameters(index = "0", description = "User ID") String userId) {
        if (!checkAuth()) return;
        List<Post> posts = postRepository.findByAuthorId(userId);
        for (Post p : posts) {
            System.out.printf("%d - %s (%s)%n", p.getId(), p.getTitle(), p.getCreatedAt());
        }
    }

    private String truncate(String val, int len) {
        if (val == null) return "";
        return val.length() > len ? val.substring(0, len - 3) + "..." : val;
    }
}
