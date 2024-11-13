package com.economy.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponse {
    public Long id;
    public Long userId;
    public String userNickname;
    public String title;
    public String content;
    public Long likeId;

}
