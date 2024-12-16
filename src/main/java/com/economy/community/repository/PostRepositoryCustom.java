package com.economy.community.repository;

import com.economy.community.domain.Post;
import com.economy.community.dto.PostResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositoryCustom {

    List<PostResponse> findAllPosts(String category, int page, int size);

    Post findPostById(Long id);

    List<PostResponse> getMyPosts(Long userId);

    void updatePostViewCount(Long id, Long increment);
}
