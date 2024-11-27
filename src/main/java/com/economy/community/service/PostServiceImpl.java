package com.economy.community.service;

import static com.economy.community.domain.QPost.post;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.domain.PostLike;
import com.economy.community.domain.QPost;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostLikesResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostLikeRepository;
import com.economy.community.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JPAQueryFactory queryFactory;
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getPosts(String category, int page, int size) {
        if (category == null) {
            throw new NullPointerException("category is null");
        }
        CommunityCategory categoryEnum = CommunityCategory.valueOfCategory(category);

        BooleanBuilder builder = new BooleanBuilder();

        // 동적 조건: 삭제되지 않은 게시글
        builder.and(post.deleted.eq(false));

//        // 동적 조건: 특정 카테고리 필터
//        if (category != null) {
//            builder.and(post.category.eq(categoryEnum));
//        }

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

    @Transactional(readOnly = true)
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


    @Transactional(readOnly = true)
    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findPostById(id);
        return PostResponse.from(post);
    }


    @Transactional
    @Override
    public CreatePostResponse createPost(CreatePostRequest request, Long userId, String userNickname) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(CommunityCategory.valueOf(request.getCategory()))
                .userId(userId)
                .userNickname(userNickname)
                .likesCount(0L)
                .viewCount(0L)
                .commentsCount(0L)
                .build();
        System.out.println("service 1");
        Post savedPost = postRepository.save(post);
        System.out.println("service 2");
        return new CreatePostResponse(savedPost);
    }

    @Transactional
    @Override
    public UpdatePostResponse updatePost(UpdatePostRequest request, Long id, Long userId) {
        Post post = postRepository.findPostById(id);

        // 사용자 검증
        post.validateUserAuthorization(userId);

        // 게시글 수정 (불변 객체 방식)
        Post updatedPost = post.withUpdatedFields(request.getTitle(), request.getContent());
        Post savedPost = postRepository.save(updatedPost);

        return new UpdatePostResponse(savedPost);
    }

    @Transactional
    @Override
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findPostById(id);

        // 사용자 검증
        post.validateUserAuthorization(userId);
        //validateUserAuthorization(post, userId);

        // 게시글 삭제 (불변 객체 방식)
        Post deletedPost = post.withDeleted();
        postRepository.save(deletedPost);
    }

    @Override
    public PostLikesResponse toggleLike(Long id, Long userId, String userNickname) {
        // 게시글 조회
        Post post = postRepository.findPostById(id);

        if (post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot like your own post.");
        }

        // 좋아요 여부 확인
        Optional<PostLike> existingLike = postLikeRepository.findByUserIdAndPostId(userId, post);

        boolean isLiked;

        if (existingLike.isEmpty()) {
            // 좋아요 추가
            PostLike newLike = PostLike.builder()
                    .userId(userId)
                    .userNickname(userNickname)
                    .postId(post)
                    .build();
            postLikeRepository.save(newLike);

            post.incrementPostLikesCount();
            isLiked = true;
        } else {
            // 좋아요 취소
            PostLike like = existingLike.get();
            postLikeRepository.delete(like);

            post.decrementLikeCount();
            isLiked = false;
        }

        // 게시글의 변경된 좋아요 수 저장
        postRepository.save(post);

        return new PostLikesResponse(isLiked, post.getLikesCount());
    }

}
