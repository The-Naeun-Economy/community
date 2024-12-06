package com.economy.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyLikedPostResponse {

    private Long id;
    private String content;
    private Long likeCount;
}
