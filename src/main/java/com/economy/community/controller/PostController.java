package com.economy.community.controller;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.jwt.JwtUtil;
import com.economy.community.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;
    private final JwtUtil jwtUtil;

    @GetMapping
    public List<PostResponse> getPostsByCategory(@RequestParam CommunityCategory category) {
        return service.getAllPosts(category);
    }

    @GetMapping("/my-posts")
    public List<PostResponse> getMyPosts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return service.getMyPosts(userId);
    }

    @GetMapping("/{id}")
    public PostResponse getCommunity(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostResponse createCommunity(@RequestBody @Valid CreatePostRequest request,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromToken(token);
        String userNickname = jwtUtil.getUserNicknameFromToken(token);

        //디버깅용
        System.out.println("User ID: " + userId + ", Nickname: " + userNickname);
        
        return service.createPost(request, userId, userNickname);
    }

    @PutMapping("/{id}")
    public UpdatePostResponse updateCommunity(@RequestBody UpdatePostRequest request,
                                              @PathVariable Long id,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return service.updatePost(request, id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommunity(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromToken(token);
        service.deletePost(id, userId);
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
    }
}
