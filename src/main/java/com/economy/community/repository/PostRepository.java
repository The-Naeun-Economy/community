package com.economy.community.repository;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.deleted = false")
    List<Post> findAllPostsByCategory(@Param("category") CommunityCategory category);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.deleted = false")
    List<Post> findAllByUserId(@Param("userId") Long userId);

    Optional<Post> findByIdAndDeletedFalse(Long id);
}
