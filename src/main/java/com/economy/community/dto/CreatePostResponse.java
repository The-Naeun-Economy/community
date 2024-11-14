package com.economy.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String userNickname;
    private String category;
    private Long likesCount;
    private Long viewCount;
    private Long commentsCount;
}
