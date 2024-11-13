package com.economy.community.service;

import com.economy.community.domain.Post;
import com.economy.community.dto.PostRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public Post getPostById(long id) {
        return null;
    }

    @Override
    public PostRequest createPost(PostRequest request) {
        return null;
    }

    @Override
    public PostRequest updatePost(PostRequest request, long id) {
        return null;
    }

    @Override
    public void deletePost(long id) {

    }
}
