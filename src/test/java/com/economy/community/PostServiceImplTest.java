package com.economy.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostRepository;
import com.economy.community.service.PostServiceImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        queryFactory = new JPAQueryFactory(entityManager);
    }

    // 성공 케이스: 게시글 생성
    @Test
    void createPost_shouldSaveAndReturnPost() {
        // Given
        CreatePostRequest request = new CreatePostRequest(
                "Post Title",
                "Post Content",
                "IT"
        );
        Long userId = 1L;
        String userNickname = "User1";

        Post mockPost = Post.builder()
                .id(1L)
                .title(request.getTitle())
                .content(request.getContent())
                .category(CommunityCategory.IT)
                .userId(userId)
                .userNickname(userNickname)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // When
        CreatePostResponse result = postService.createPost(request, userId, userNickname);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L); // 필드 이름 일치 확인
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    void createPost_shouldThrowExceptionWhenSaveFails() {
        // Given
        CreatePostRequest request = new CreatePostRequest(
                "Post Title",
                "Post Content",
                "IT"
        );
        Long userId = 1L;
        String userNickname = "User1";

        when(postRepository.save(any(Post.class))).thenThrow(new RuntimeException("Database error"));

        // When / Then
        assertThatThrownBy(() -> postService.createPost(request, userId, userNickname))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }


    // 성공 케이스: 게시글 수정
    @Test
    void updatePost_shouldUpdateAndReturnPost_whenAuthorizedUser() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Content");

        Post existingPost = Post.builder()
                .id(postId)
                .title("Original Title")
                .content("Original Content")
                .userId(userId)
                .userNickname("User1")
                .build();

        when(postRepository.findPostById(postId)).thenReturn(existingPost);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UpdatePostResponse result = postService.updatePost(request, postId, userId);

        // Then
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();

        assertThat(capturedPost.getTitle()).isEqualTo(request.getTitle());
        assertThat(capturedPost.getContent()).isEqualTo(request.getContent());
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        assertThat(result.getContent()).isEqualTo(request.getContent());
    }

    // 실패 케이스: 다른 사용자가 게시글 수정
    @Test
    void updatePost_shouldThrowException_whenUnauthorizedUser() {
        // Given
        Long postId = 1L;
        Long requestUserId = 1L; // 요청 사용자의 ID
        Long ownerUserId = 2L; // 게시글 작성자의 ID
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Content");

        // 게시글 생성 (작성자는 다른 사용자)
        Post existingPost = Post.builder()
                .id(postId)
                .title("Old Title")
                .content("Old Content")
                .userId(ownerUserId) // 게시글 작성자의 ID
                .userNickname("User2")
                .build();

        // Mock 설정: findPostById가 기존 게시글을 반환하도록 설정
        when(postRepository.findPostById(postId)).thenReturn(existingPost);

        // When / Then
        assertThatThrownBy(() -> postService.updatePost(request, postId, requestUserId))
                .isInstanceOf(IllegalArgumentException.class) // 예외 타입 변경
                .hasMessage("You are not authorized to perform this action on this post"); // 예외 메시지 확인

        // Verify: findPostById는 한 번 호출되어야 함
        verify(postRepository, times(1)).findPostById(postId);

        // Verify: 저장(save) 메서드는 호출되지 않아야 함
        verify(postRepository, never()).save(any(Post.class));
    }

    // 성공 케이스: 게시글 삭제
    @Test
    void deletePost_shouldDeletePost_whenAuthorizedUser() {
        // Given
        Long postId = 1L;
        Long userId = 1L; // 요청 사용자의 ID
        Post existingPost = Post.builder()
                .id(postId)
                .title("Post Title")
                .content("Post Content")
                .category(CommunityCategory.IT)
                .userId(userId) // 게시글 작성자의 ID
                .userNickname("User1")
                .build();

        // Mock 설정: findPostById가 게시글 반환
        when(postRepository.findPostById(postId)).thenReturn(existingPost);

        // Mock 설정: save는 호출된 객체를 반환
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        postService.deletePost(postId, userId);

        // Then
        verify(postRepository, times(1)).findPostById(postId); // 게시글 조회 확인
        verify(postRepository, times(1)).save(any(Post.class)); // 저장 확인

        // 삭제된 상태 검증
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        Post deletedPost = postCaptor.getValue();
        assertThat(deletedPost.isDeleted()).isTrue(); // isDeleted는 삭제 여부를 나타내는 메서드라고 가정
    }

    // 실패 케이스: 다른 사용자가 게시글 삭제
    @Test
    void deletePost_shouldThrowExceptionWhenUnauthorizedUser() {
        // Given
        Long postId = 1L;
        Long userId = 1L; // 요청 사용자의 ID
        Long ownerUserId = 2L; // 게시글 작성자의 ID
        Post existingPost = Post.builder()
                .id(postId)
                .title("Post Title")
                .content("Post Content")
                .category(CommunityCategory.IT)
                .userId(ownerUserId) // 게시글 작성자의 ID
                .userNickname("User2")
                .build();

        // Mock 설정: findPostById가 게시글 반환
        when(postRepository.findPostById(postId)).thenReturn(existingPost);

        // When / Then
        assertThatThrownBy(() -> postService.deletePost(postId, userId)) // userId 전달
                .isInstanceOf(IllegalArgumentException.class) // 예외 타입 확인
                .hasMessage("You are not authorized to perform this action on this post"); // 예외 메시지 확인

        // Verify: save 메서드는 호출되지 않아야 함
        verify(postRepository, never()).save(any(Post.class));
    }

}
