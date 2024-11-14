package com.economy.community.service;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.DeletePostRequest;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostResponse> getAllPosts(CommunityCategory category) {
        List<Post> posts = postRepository.findAllPostsByCategory(category);
        return posts.stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id" + id));
        return convertToPostResponse(post);
    }

    @Override
    public CreatePostResponse createPost(CreatePostRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userNickname = ((UserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserNickname();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(CommunityCategory.valueOf(request.getCategory()))
                .likesCount(0L)
                .viewCount(0L)
                .commentsCount(0L)
                .build();
        Post savedPost = postRepository.save(post);
        return convertToPostResponse(savedPost);
    }

    @Override
    public UpdatePostResponse updatePost(UpdatePostRequest request, long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this post");
        }

        Post updatedPost = Post.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .userNickname(post.getUserNickname())
                .title(request.getTitle())
                .content(request.getContent())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .likesCount(post.getLikesCount())
                .viewCount(post.getViewCount())
                .commentsCount(post.getCommentsCount())
                .deleted(true)
                .build();
        Post savedPost = postRepository.save(updatedPost);
        return convertToPostResponse(savedPost);
    }

    @Override
    public DeletePostRequest deletePost(DeletePostRequest request, long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    private PostResponse convertToPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikesCount(),
                post.getViewCount(),
                post.getCommentsCount()
        );
}
