package com.economy.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostRepository;
import com.economy.community.service.PostServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_ShouldReturnCreatePostResponse() {
        CreatePostRequest request = CreatePostRequest.builder()
                .title("Title")
                .content("Content")
                .category("IT")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .category(CommunityCategory.IT)
                .userId(1L)
                .userNickname("UserNickname")
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        CreatePostResponse response = postService.createPost(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getCategory()).isEqualTo("IT");
    }

    @Test
    void updatePost_ShouldReturnUpdatePostResponse() {
        UpdatePostRequest request = UpdatePostRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .category(CommunityCategory.IT)
                .userId(1L)
                .userNickname("UserNickname")
                .deleted(false)
                .build();

        Post updatedPost = post.withUpdatedFields(request.getTitle(), request.getContent());

        when(postRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        UpdatePostResponse response = postService.updatePost(request, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Title");
        assertThat(response.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void deletePost_ShouldDeletePost() {
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .category(CommunityCategory.IT)
                .userId(1L)
                .userNickname("UserNickname")
                .deleted(false)
                .build();

        when(postRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(post));

        Post deletedPost = post.withDeleted();
        when(postRepository.save(any(Post.class))).thenReturn(deletedPost);

        postService.deletePost(1L);

        verify(postRepository, times(1)).save(any(Post.class));
        assertThat(deletedPost.isDeleted()).isTrue();
    }

    @Test
    void getAllPosts_ShouldReturnPostList() {
        // 테스트용 게시글 목록 생성
        List<Post> posts = Arrays.asList(
                Post.builder()
                        .id(1L)
                        .title("Title1")
                        .content("Content1")
                        .category(CommunityCategory.IT)
                        .deleted(false)
                        .build(),
                Post.builder()
                        .id(2L)
                        .title("Title2")
                        .content("Content2")
                        .category(CommunityCategory.IT)
                        .deleted(false)
                        .build()
        );

        // Mock 설정: findAllPostsByCategory가 게시글 목록을 반환하도록 설정
        when(postRepository.findAllPostsByCategory(CommunityCategory.IT)).thenReturn(posts);

        // 서비스 메서드 호출
        List<PostResponse> response = postService.getAllPosts(CommunityCategory.IT);

        // 반환된 결과 검증
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getTitle()).isEqualTo("Title1");
        assertThat(response.get(1).getTitle()).isEqualTo("Title2");
    }

    @Test
    void getPostById_ShouldReturnPost() {
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .category(CommunityCategory.IT)
                .deleted(false)
                .build();

        // Mock 설정: findByIdAndDeletedFalse가 게시글을 반환하도록 설정
        when(postRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(post));

        // 서비스 메서드 호출
        PostResponse response = postService.getPostById(1L);

        // 반환된 결과 검증
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getContent()).isEqualTo("Content");
    }
}
