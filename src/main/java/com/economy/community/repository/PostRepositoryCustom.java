package com.economy.community.repository;

import com.economy.community.dto.PostResponse;
import java.util.List;

public interface PostRepositoryCustom {
    List<PostResponse> findPostsByCategory(String category);

    List<PostResponse> findAllPosts();
}
