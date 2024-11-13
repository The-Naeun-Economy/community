package com.economy.community.service;

import com.economy.community.domain.Post;
import com.economy.community.dto.PostRequest;
import java.util.List;

public abstract interface PostService {
    public List<Post> getAllPosts();

    public Post getPostById(long id);

    public PostRequest createPost(PostRequest request);

    public PostRequest updatePost(PostRequest request, long id);

    public void deletePost(long id);
}
