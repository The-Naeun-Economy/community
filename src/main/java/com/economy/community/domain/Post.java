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
}
