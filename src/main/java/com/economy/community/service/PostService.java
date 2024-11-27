package com.economy.community.service;

import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostLikesResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import java.util.List;

public interface PostService {
    List<PostResponse> getPosts(String category, int page, int size);

    List<PostResponse> getMyPosts(Long userId);

    PostResponse getPostById(Long id);

    CreatePostResponse createPost(CreatePostRequest request, Long userId, String userNickname);

    UpdatePostResponse updatePost(UpdatePostRequest request, Long id, Long userId);

    void deletePost(Long id, Long userId);

    PostLikesResponse toggleLike(Long id, Long userId, String userNickname);
}
