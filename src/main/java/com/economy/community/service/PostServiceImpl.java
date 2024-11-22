package com.economy.community.service;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getAllPosts(CommunityCategory category) {
        List<Post> posts = postRepository.findAllPostsByCategory(category);
        return posts.stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> getMyPosts(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        return posts.stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponse getPostById(long id) {
        Post post = findPostById(id);
        return convertToPostResponse(post);
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
        Post savedPost = postRepository.save(post);

        return new CreatePostResponse(savedPost);
    }

    @Transactional
    @Override
    public UpdatePostResponse updatePost(UpdatePostRequest request, Long id, Long userId) {
        Post post = findPostById(id);

        // 사용자 검증
        validateUserAuthorization(post, userId);

        // 게시글 수정 (불변 객체 방식)
        Post updatedPost = post.withUpdatedFields(request.getTitle(), request.getContent());
        Post savedPost = postRepository.save(updatedPost);

        return new UpdatePostResponse(savedPost);
    }

    @Transactional
    @Override
    public void deletePost(Long id, Long userId) {
        Post post = findPostById(id);

        // 사용자 검증
        validateUserAuthorization(post, userId);

        // 게시글 삭제 (불변 객체 방식)
        Post deletedPost = post.withDeleted();
        postRepository.save(deletedPost);
    }

    // 게시글 조회
    private Post findPostById(Long id) {
        return postRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
    }

    // 사용자 검증 로직
    private void validateUserAuthorization(Post post, Long userId) {
        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to perform this action on this post");
        }
    }

    // Post -> PostResponse 변환
    private PostResponse convertToPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                post.getUserNickname(),
                post.getCreatedAt(),
                post.getLikesCount(),
                post.getViewCount(),
                post.getCommentsCount()
        );
    }
}
