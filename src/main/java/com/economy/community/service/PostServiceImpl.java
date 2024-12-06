package com.economy.community.service;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.domain.PostLike;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.GetMyLikedPostResponse;
import com.economy.community.dto.PostLikesResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostCacheRepository;
import com.economy.community.repository.PostLikeRepository;
import com.economy.community.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCacheRepository postCacheRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(String category, int page, int size) {
        return postRepository.findAllPosts(category, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getMyPosts(Long userId) {
        return postRepository.getMyPosts(userId);
    }


    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findPostById(id);
        return PostResponse.from(post);
    }


    @Override
    @Transactional
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
        Post savedPost = postRepository.save(post);

        // Response 반환
        Long likeCount = 0L;
        return new CreatePostResponse(savedPost, likeCount);
    }

    @Override
    @Transactional
    public UpdatePostResponse updatePost(UpdatePostRequest request, Long id, Long userId) {
        Post post = postRepository.findPostById(id);

        // 사용자 검증
        post.validateUserAuthorization(userId);

        // 게시글 수정 (불변 객체 방식)
        Post updatedPost = post.withUpdatedFields(request.getTitle(), request.getContent());
        Post savedPost = postRepository.save(updatedPost);

        // 좋아요 수를 Redis에서 조회
        Long likeCount = postCacheRepository.getLikeCount(savedPost.getId());

        return new UpdatePostResponse(savedPost, likeCount);
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findPostById(id);

        // 사용자 검증
        post.validateUserAuthorization(userId);

        // 게시글 삭제 (불변 객체 방식)
        Post deletedPost = post.withDeleted();
        postRepository.save(deletedPost);
    }

    @Override
    @Transactional
    public PostLikesResponse toggleLike(Long id, Long userId, String userNickname) {
        // 게시글 조회
        Post post = postRepository.findPostById(id);

        if (post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot like your own post.");
        }

        // 좋아요 여부 확인
        Optional<PostLike> existingLike = postLikeRepository.findByUserIdAndPostId(userId, post.getId());

        boolean isLiked;

        if (existingLike.isEmpty()) {
            // 중복 방지: 유니크 제약 조건을 통해 데이터베이스 레벨에서 중복 방지
            PostLike newLike = PostLike.builder()
                    .userId(userId)
                    .userNickname(userNickname)
                    .post(post)
                    .build();
            postLikeRepository.save(newLike);

            postCacheRepository.incrementLikeCount(id); // Redis에서 좋아요 수 증가
            isLiked = true;
        } else {
            // 좋아요 취소
            PostLike like = existingLike.get();
            postLikeRepository.delete(like);

            postCacheRepository.decrementLikeCount(id); // Redis에서 좋아요 수 감소
            isLiked = false;
        }

        // Redis에서 최신 좋아요 수 가져오기
        Long updatedLikeCount = postCacheRepository.getLikeCount(id);

        // 좋아요 수 동기화
        post.syncLikesCount(updatedLikeCount);
        postRepository.save(post);

        return new PostLikesResponse(isLiked, updatedLikeCount);
    }

    @Override
    @Transactional
    public List<GetMyLikedPostResponse> getMyLikedPosts(Long userId) {
        List<PostLike> likedPosts = postLikeRepository.findByUserId(userId);

        return likedPosts.stream()
                .map(like -> new GetMyLikedPostResponse(
                        like.getPost().getId(),
                        like.getPost().getContent(),
                        like.getPost().getLikesCount()
                ))
                .collect(Collectors.toList());
    }

}
