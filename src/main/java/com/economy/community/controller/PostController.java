package com.economy.community.controller;

import com.economy.community.dto.*;
import com.economy.community.jwt.TokenProvider;
import com.economy.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;
    private final TokenProvider tokenProvider;

    @GetMapping
    public List<PostResponse> getPosts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.out.print("run");
        return service.getPosts(category, page, size);
    }

    @GetMapping("/users/me")
    public List<PostResponse> getMyPosts(@RequestHeader String Authorization) {
        Long userId = tokenProvider.getUserIdFromToken(Authorization);
        return service.getMyPosts(userId);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostResponse createCommunity(@RequestBody @Valid CreatePostRequest request,
                                              @RequestHeader String Authorization) {
        Long userId = tokenProvider.getUserIdFromToken(Authorization);
        String userNickname = tokenProvider.getNickNameFromToken(Authorization);

        return service.createPost(request, userId, userNickname);
    }

    @PutMapping("/{id}")
    public UpdatePostResponse updateCommunity(@RequestBody UpdatePostRequest request,
                                              @PathVariable Long id,
                                              @RequestHeader String Authorization) {
        Long userId = tokenProvider.getUserIdFromToken(Authorization);
        return service.updatePost(request, id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCommunity(@PathVariable Long id, @RequestHeader String Authorization) {
        Long userId = tokenProvider.getUserIdFromToken(Authorization);
        service.deletePost(id, userId);
    }

    // 게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<PostLikesResponse> likePost(@PathVariable Long id, @RequestHeader String Authorization) {
        Long userId = tokenProvider.getUserIdFromToken(Authorization);
        String userNickname = tokenProvider.getNickNameFromToken(Authorization);

        PostLikesResponse response = service.toggleLike(id, userId, userNickname);
        return ResponseEntity.ok(response);
    }
}
