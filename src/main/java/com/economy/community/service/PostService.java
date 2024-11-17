package com.economy.community.service;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import java.util.List;

public interface PostService {
    List<PostResponse> getAllPosts(CommunityCategory category);

    List<PostResponse> getMyPosts(Long userId);

    PostResponse getPostById(long id);

    CreatePostResponse createPost(CreatePostRequest request);

    UpdatePostResponse updatePost(UpdatePostRequest request, long id);

    void deletePost(long id);
}
