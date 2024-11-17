package com.economy.community.controller;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public List<PostResponse> getPostsByCategory(@RequestParam CommunityCategory category) {
        return service.getAllPosts(category);
    }

    @GetMapping("/my-posts")
    public List<PostResponse> getMyPosts(@RequestHeader("Authorization") String authorizationHeader) {
        // JWT에서 사용자 정보 추출
        Long userId = extractUserIdFromJwt(authorizationHeader);
        return service.getMyPosts(userId);
    }

    private Long extractUserIdFromJwt(String authorizationHeader) {
        // JWT 파싱 로직 (테스트 환경에서는 하드코딩된 값 사용 가능)
        String token = authorizationHeader.replace("Bearer ", "");
        return 1L; // 하드코딩된 값 (실제 JWT 파싱 로직 추가 필요)
    }

    @GetMapping("/{id}")
    public PostResponse getCommunity(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostResponse createCommunity(@RequestBody @Valid CreatePostRequest request) {
        return service.createPost(request);
    }

    @PutMapping("/{id}")
    public UpdatePostResponse updateCommunity(@RequestBody UpdatePostRequest request, @PathVariable Long id) {
        return service.updatePost(request, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommunity(@PathVariable Long id) {
        service.deletePost(id);
    }
}
