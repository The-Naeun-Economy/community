package com.economy.community.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponse {
    private Long id;
    private Long userId;
    private String userNickname;
    private String title;
    private String content;
    private Long likeId;
    private LocalDateTime createdAt;

}
