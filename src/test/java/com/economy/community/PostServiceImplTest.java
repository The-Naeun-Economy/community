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
        Long userId = 1L; // 수정하려는 사용자의 ID 동일
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated Title",
                "Updated Content"
        );

        Post existingPost = Post.builder()
                .id(postId)
                .title("Old Title")
                .content("Old Content")
                .category(CommunityCategory.IT)
                .userId(userId) // 게시글의 작성자 ID
                .userNickname("User1")
                .build();

        Post updatedPost = existingPost.withUpdatedFields(request.getTitle(), request.getContent());

        when(postRepository.findPostById(postId)).thenReturn(existingPost);
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        // When
        UpdatePostResponse result = postService.updatePost(request, postId, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        assertThat(result.getContent()).isEqualTo(request.getContent());

        // 기존 게시글과 사용자 검증 확인
        verify(postRepository, times(1)).findPostById(postId);
        verify(postRepository, times(1)).save(updatedPost);
    }

//    // 실패 케이스: 다른 사용자가 게시글 수정
//    @Test
//void updatePost_shouldThrowException_whenUnauthorizedUser() {
//    // Given
//    Long postId = 1L;
//    Long userId = 1L; // 요청한 사용자
//    Long otherUserId = 2L; // 게시글 작성자와 다른 사용자
//    UpdatePostRequest request = new UpdatePostRequest(
//            "Updated Title",
//            "Updated Content"
//    );
//
//    Post existingPost = Post.builder()
//            .id(postId)
//            .title("Old Title")
//            .content("Old Content")
//            .category(CommunityCategory.IT)
//            .userId(otherUserId) // 다른 사용자가 작성한 게시글
//            .userNickname("User2")
//            .build();
//
//    when(postRepository.findPostById(postId)).thenReturn(existingPost);
//
//    // When / Then
//    assertThatThrownBy(() -> postService.updatePost(request, postId, userId))
//            .isInstanceOf(UserNotAuthorizedException.class) // 적절한 예외 클래스 사용
//            .hasMessage("User is not authorized to modify this post");
//
//    // 게시글 찾기만 시도, 저장은 호출되지 않음
//    verify(postRepository, times(1)).findPostById(postId);
//    verify(postRepository, times(0)).save(any(Post.class));
//}
//
//
//    // 성공 케이스: 게시글 삭제
//    @Test
//    void deletePost_shouldDeletePost() {
//        // Given
//        Long postId = 1L;
//        Post existingPost = Post.builder()
//                .id(postId)
//                .title("Post Title")
//                .content("Post Content")
//                .category(CommunityCategory.IT)
//                .userId(1L)
//                .userNickname("User1")
//                .build();
//
//        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
//        doNothing().when(postRepository).delete(existingPost);
//
//        // When
//        postService.deletePost(postId);
//
//        // Then
//        verify(postRepository, times(1)).delete(existingPost);
//    }
//
//
//    // 실패 케이스: 다른 사용자가 게시글 삭제
//    @Test
//    void deletePost_shouldThrowExceptionWhenPostNotFound() {
//        // Given
//        Long postId = 1L;
//
//        when(postRepository.findById(postId)).thenReturn(Optional.empty());
//
//        // When / Then
//        assertThatThrownBy(() -> postService.deletePost(postId))
//                .isInstanceOf(PostNotFoundException.class)
//                .hasMessage("Post with id 1 not found");
//    }

}
