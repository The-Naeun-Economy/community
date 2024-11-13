package com.economy.community.service;

import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.DeletePostRequest;
import com.economy.community.dto.PostRequest;
import com.economy.community.dto.UpdatePostRequest;

public abstract interface PostService {
    public PostRequest getAllPosts();

    public PostRequest getPostById(long id);

    public CreatePostRequest createPost(PostRequest request);

    public UpdatePostRequest updatePost(PostRequest request, long id);

    public DeletePostRequest deletePost(long id);
}
