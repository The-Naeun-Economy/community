package com.economy.community.controller;

import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.DeletePostRequest;
import com.economy.community.dto.PostRequest;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public PostRequest getPostsByCategory() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostRequest getCommunity(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostRequest createCommunity(@RequestBody PostRequest request) {
        return service.createPost(request);
    }

    @PutMapping("/{id}")
    public UpdatePostRequest updateCommunity(@RequestBody PostRequest request, @PathVariable Long id) {
        return service.updatePost(request, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeletePostRequest deleteCommunity(@PathVariable Long id) {
        return service.deletePost(id);
    }
}
