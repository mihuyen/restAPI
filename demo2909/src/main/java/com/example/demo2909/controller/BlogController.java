package com.example.demo2909.controller;

import com.example.demo2909.dto.BlogRequest;
import com.example.demo2909.entity.Blog;
import com.example.demo2909.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(blogService.getAllBlogs());
        } else {
            return ResponseEntity.ok(blogService.getBlogsByUsername(authentication.getName()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @blogService.isOwner(#id, authentication.name)")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody BlogRequest blogRequest, Authentication authentication) {
        return ResponseEntity.ok(blogService.createBlog(blogRequest, authentication.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @blogService.isOwner(#id, authentication.name)")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.updateBlog(id, blogRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @blogService.isOwner(#id, authentication.name)")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok().build();
    }
}