package com.economy.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import com.economy.community.domain.QPost;
import com.economy.community.dto.CreatePostRequest;
import com.economy.community.dto.CreatePostResponse;
import com.economy.community.dto.PostResponse;
import com.economy.community.dto.UpdatePostRequest;
import com.economy.community.dto.UpdatePostResponse;
import com.economy.community.repository.PostRepository;
import com.economy.community.service.PostServiceImpl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    // 성공 케이스: 카테고리별 게시글 조회
    @Test
    void getPosts_shouldReturnPostList() {
        // Given
        String category = "IT";
        int page = 0;
        int size = 10;

        PostResponse mockPostResponse = new PostResponse(
                1L,
                CommunityCategory.IT,
                "Post 1",
                "Content 1",
                1L,
                "User1",
                LocalDateTime.now(),
                10L,
                100L,
                5L
        );

        QPost post = QPost.post;

        // Mock QueryDSL의 JPAQuery 객체
        JPAQuery<PostResponse> mockQuery = Mockito.mock(JPAQuery.class);

        // QueryDSL select() Mock 설정
        when(queryFactory.select(any(com.querydsl.core.types.Expression.class))).thenReturn(mockQuery);
        when(mockQuery.from(post)).thenReturn(mockQuery);

        // where 메서드 Mock 설정 (Predicate 타입 지정)
        when(mockQuery.where(any(com.querydsl.core.types.Predicate.class))).thenReturn(mockQuery);

        // 기타 체이닝 메서드 Mock 설정
        when(mockQuery.offset(anyLong())).thenReturn(mockQuery);
        when(mockQuery.limit(anyLong())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(mockPostResponse));

        // When
        List<PostResponse> result = postService.getPosts(category, page, size);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Post 1");
    }

    // 실패 케이스: 잘못된 카테고리 예외
    @Test
    void getPosts_invalidCategoryShouldThrowException() {
        // Given
        String invalidCategory = "INVALID";

        // When & Then
        assertThatThrownBy(() -> postService.getPosts(invalidCategory, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid category: INVALID");
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

    // 실패 케이스: 잘못된 카테고리로 생성
    @Test
    void createPost_invalidCategoryShouldThrowException() {
        // Given
        CreatePostRequest request = new CreatePostRequest(
                "Post Title",
                "Post Content",
                "INVALID_CATEGORY"
        );

        // When & Then
        assertThatThrownBy(() -> postService.createPost(request, 1L, "User1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid category: INVALID_CATEGORY");
    }

    // 성공 케이스: 게시글 수정
    @Test
    void updatePost_shouldUpdateAndReturnUpdatedPost() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated Title",
                "Updated Content"
        );

        Post existingPost = Post.builder()
                .id(postId)
                .title("Old Title")
                .content("Old Content")
                .userId(userId)
                .build();

        Post updatedPost = Post.builder()
                .id(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .userId(userId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        // When
        UpdatePostResponse result = postService.updatePost(request, postId, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(postId); // 필드 이름 일치 확인
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
    }

    // 실패 케이스: 다른 사용자가 게시글 수정
    @Test
    void updatePost_shouldThrowExceptionForUnauthorizedUser() {
        // Given
        Long postId = 1L;
        Long unauthorizedUserId = 2L; // 다른 사용자
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated Title",
                "Updated Content"
        );

        Post existingPost = Post.builder()
                .id(postId)
                .userId(1L) // 실제 소유자
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // When & Then
        assertThatThrownBy(() -> postService.updatePost(request, postId, unauthorizedUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("You are not authorized to perform this action on this post");
    }

    // 성공 케이스: 게시글 삭제
    @Test
    void deletePost_shouldMarkPostAsDeleted() {
        // Given
        Long postId = 1L;
        Long userId = 1L;

        Post existingPost = Post.builder()
                .id(postId)
                .userId(userId)
                .deleted(false)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // When
        postService.deletePost(postId, userId);

        // Then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    // 실패 케이스: 다른 사용자가 게시글 삭제
    @Test
    void deletePost_shouldThrowExceptionForUnauthorizedUser() {
        // Given
        Long postId = 1L;
        Long unauthorizedUserId = 2L; // 다른 사용자

        Post existingPost = Post.builder()
                .id(postId)
                .userId(1L) // 실제 소유자
                .deleted(false)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // When & Then
        assertThatThrownBy(() -> postService.deletePost(postId, unauthorizedUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("You are not authorized to perform this action on this post");
    }
}
