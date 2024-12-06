package com.economy.community.event;

import com.economy.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCacheUpdate {

    private Long postId;
    private Post post;
    private Long userId;
    private String userNickname;
}
