package com.economy.community.controller;

import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostLikesResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public List<PostResponse> getPosts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.getPosts(category, page, size);
    }

    @GetMapping("/users/me")
    public List<PostResponse> getMyPosts() {
        Long userId = getAuthenticatedUserId();
        return service.getMyPosts(userId);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostResponse createCommunity(@RequestBody @Valid CreatePostRequest request) {
        Long userId = getAuthenticatedUserId();
        String userNickname = getAuthenticatedUserNickname();

        return service.createPost(request, userId, userNickname);
    }

    @PutMapping("/{id}")
    public UpdatePostResponse updateCommunity(@RequestBody UpdatePostRequest request,
                                              @PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        return service.updatePost(request, id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCommunity(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        service.deletePost(id, userId);
    }

    // 게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<PostLikesResponse> likePost(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        String userNickname = getAuthenticatedUserNickname();

        PostLikesResponse response = service.toggleLike(id, userId, userNickname);
        return ResponseEntity.ok(response);
    }

    // 인증된 사용자 ID 가져오기
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        return (Long) authentication.getPrincipal();
    }

    // 인증된 사용자 닉네임 가져오기
    private String getAuthenticatedUserNickname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        return (String) authentication.getCredentials();
    }
}
