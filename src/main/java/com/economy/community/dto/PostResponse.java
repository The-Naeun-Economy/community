package com.economy.community.dto;

import com.economy.community.domain.CommunityCategory;
import com.economy.community.domain.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private CommunityCategory category;
    private String title;
    private String content;
    private Long userId;
    private String userNickname;
    private LocalDateTime createdAt;
    private Long likesCount;
    private Long viewCount;
    private Long commentsCount;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getCategory(),
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

    // 좋아요 수를 업데이트한 새로운 PostResponse 반환
    public PostResponse withUpdatedLikesCount(Long likeCount) {
        return new PostResponse(
                this.id,
                this.category,
                this.title,
                this.content,
                this.userId,
                this.userNickname,
                this.createdAt,
                likeCount, // 새로운 좋아요 수
                this.viewCount,
                this.commentsCount
        );
    }

    // 조회수 업데이트한 새로운 PostResponse 반환
    public PostResponse withUpdatedViewCount(Long viewCount) {
        return new PostResponse(
                this.id,
                this.category,
                this.title,
                this.content,
                this.userId,
                this.userNickname,
                this.createdAt,
                this.likesCount,
                viewCount, // 새로운 조회수
                this.commentsCount
        );
    }
}
