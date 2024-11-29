package com.economy.community.repository;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.domain.QPost;
import com.economy.community.dto.PostResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostResponse> findAllPosts(String category, int page, int size) {
        QPost post = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();

        // 동적 조건: 삭제되지 않은 게시글
        builder.and(post.deleted.eq(false));

        if (category != null) {
            CommunityCategory categoryEnum = CommunityCategory.valueOfCategory(category);
            builder.and(post.category.eq(categoryEnum));
        }

        return queryFactory
                .select(Projections.constructor(PostResponse.class,
                        post.id,
                        post.category,
                        post.title,
                        post.content,
                        post.userId,
                        post.userNickname,
                        post.createdAt,
                        post.likesCount,
                        post.viewCount,
                        post.commentsCount))
                .from(post)
                .where(builder)
                .orderBy(post.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
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

    @Override
    public List<PostResponse> getMyPosts(Long userId) {
        QPost post = QPost.post;

        return queryFactory
                .select(Projections.constructor(PostResponse.class,
                        post.id,
                        post.category,
                        post.title,
                        post.content))
                .from(post)
                .where(post.userId.eq(userId).and(post.deleted.eq(false)))
                .fetch();
    }
}
