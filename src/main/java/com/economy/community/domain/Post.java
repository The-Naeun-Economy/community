package com.economy.community.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @NotNull
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
    @Column(name = "create_date")
    private LocalDateTime createdAt;
    @Column(name = "update_date")
    private LocalDateTime updatedAt;
    @Column(name = "like_id")
    private Long likeId;
}
