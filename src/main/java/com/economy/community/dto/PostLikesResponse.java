package com.economy.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikesResponse {

    private boolean isLiked;
    private Long likeCnt;
}
