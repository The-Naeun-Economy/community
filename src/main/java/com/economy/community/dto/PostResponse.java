package com.economy.community.dto;

import com.economy.community.domain.CommunityCategory;
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
}
