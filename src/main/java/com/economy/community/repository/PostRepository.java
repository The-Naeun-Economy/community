package com.economy.community.repository;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllPostsByCategory(CommunityCategory category);

    Optional<Post> findByIdAndDeletedFalse(Long id);
}
