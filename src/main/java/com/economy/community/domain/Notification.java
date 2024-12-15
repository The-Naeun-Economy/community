package com.economy.community.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Notification {

    private Long postId;          // 게시글 ID
    private String message;       // 알림 메시지
    private String details;       // 알림 상세 정보 (게시글/댓글 내용)
    private LocalDateTime createdAt; // 알림 생성 시간
}
