package com.economy.community.controller;

import com.economy.community.domain.Post;
import com.economy.community.dto.PostRequest;
import com.economy.community.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public List<Post> getCommunities() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getCommunity(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    public PostRequest createCommunity(@RequestBody PostRequest request) {
        service.createPost(request);
        return null;
    }

    @PutMapping("/{id}")
    public PostRequest updateCommunity(@RequestBody PostRequest request, @PathVariable Long id) {
        service.updatePost(request, id);
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable Long id) {
        service.deletePost(id);
    }
}
