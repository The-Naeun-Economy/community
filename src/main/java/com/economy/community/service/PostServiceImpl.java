package com.economy.community.service;

import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.DeletePostRequest;
import com.economy.community.dto.PostRequest;
import com.economy.community.dto.UpdatePostRequest;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public PostRequest getAllPosts() {
        return null;
    }

    @Override
    public PostRequest getPostById(long id) {
        return null;
    }

    @Override
    public CreatePostRequest createPost(PostRequest request) {
        return null;
    }

    @Override
    public UpdatePostRequest updatePost(PostRequest request, long id) {
        return null;
    }

    @Override
    public DeletePostRequest deletePost(long id) {
        return null;
    }
}
