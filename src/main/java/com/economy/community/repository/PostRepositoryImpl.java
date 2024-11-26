package com.economy.community.repository;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.domain.QPost;
import com.economy.community.dto.PostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostResponse> findPostsByCategory(String category) {
        QPost post = QPost.post;

        CommunityCategory categoryEnum = parseCategory(category);

        return queryFactory
                .select(Projections.constructor(PostResponse.class,
                        post.id,
                        post.category,
                        post.title,
                        post.content))
                .from(post)
                .where(post.category.eq(CommunityCategory.valueOf(category))
                        .and(post.deleted.eq(false)))
                .fetch();
    }

    @Override
    public List<PostResponse> findAllPosts() {
        QPost post = QPost.post;

        return queryFactory
                .select(Projections.constructor(PostResponse.class,
                        post.id,
                        post.category,
                        post.title,
                        post.content))
                .from(post)
                .where(post.deleted.eq(false))
                .fetch();
    }

    private CommunityCategory parseCategory(String category) {
        try {
            return CommunityCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category, e);
        }
    }

    @Override
    public Post findPostById(Long id) {
        QPost post = QPost.post;

        Post result = queryFactory
                .selectFrom(post)
                .where(post.id.eq(id).and(post.deleted.eq(false)))
                .fetchOne();

        if (result == null) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }

        return result;
    }
}
