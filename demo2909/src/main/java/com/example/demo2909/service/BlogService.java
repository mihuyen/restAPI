package com.example.demo2909.service;

import com.example.demo2909.dto.BlogRequest;
import com.example.demo2909.entity.Blog;
import com.example.demo2909.entity.User;
import com.example.demo2909.repository.BlogRepository;
import com.example.demo2909.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public Blog createBlog(BlogRequest blogRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setUser(user);

        return blogRepository.save(blog);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    public Blog updateBlog(Long id, BlogRequest blogRequest) {
        Blog blog = getBlogById(id);
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public boolean isOwner(Long blogId, String username) {
        Blog blog = getBlogById(blogId);
        return blog.getUser().getUsername().equals(username);
    }
}