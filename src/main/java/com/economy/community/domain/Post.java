package com.economy.community.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "user_nickname")
    @NotNull
    private String userNickname;

    @Column(name = "post_title")
    @NotNull
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "community_category")
    @Enumerated(EnumType.STRING)
    private CommunityCategory category;

    @Column(name = "like_count")
    private Long likesCount = 0L;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "comment_count")
    private Long commentsCount = 0L;

    @Column(name = "deleted")
    @NotNull
    private boolean deleted = false;


    public Post withUpdatedFields(String title, String content) {
        return Post.builder()
                .id(this.id)
                .userId(this.userId)
                .userNickname(this.userNickname)
                .title(title)  // 새로운 제목으로 설정
                .content(content)  // 새로운 내용으로 설정
                .category(this.category)
                .likesCount(this.likesCount)
                .viewCount(this.viewCount)
                .commentsCount(this.commentsCount)
                .deleted(this.deleted)
                .build();
    }

    public Post withDeleted() {
        return Post.builder()
                .id(this.id)
                .userId(this.userId)
                .userNickname(this.userNickname)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .likesCount(this.likesCount)
                .viewCount(this.viewCount)
                .commentsCount(this.commentsCount)
                .deleted(true)  // 삭제 상태로 설정
                .build();
    }

    public void incrementPostLikesCount() {
        if (this.likesCount == null) {
            this.likesCount = 1L;
        } else {
            this.likesCount += 1;
        }
    }

    public void decrementLikeCount() {
        if (this.likesCount > 0) {
            this.likesCount -= 1;
        }
    }
}
