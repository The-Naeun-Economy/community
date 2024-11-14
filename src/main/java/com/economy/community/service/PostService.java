package com.economy.community.service;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.DeletePostRequest;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import java.util.List;

public abstract interface PostService {
    public List<PostResponse> getAllPosts(CommunityCategory category);

    public PostResponse getPostById(long id);

    public CreatePostResponse createPost(CreatePostRequest request);

    public UpdatePostResponse updatePost(UpdatePostRequest request, long id);

    public void deletePost(DeletePostRequest request, long id);
}
