package com.economy.community.dto;

import com.economy.community.domain.Post;
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
    private Long likeCount;
    private Long viewCount;
    private Long commentsCount;

    public CreatePostResponse(Post post, Long likeCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId = post.getUserId();
        this.userNickname = post.getUserNickname();
        this.category = post.getCategory().name();
        this.likeCount = likeCount;
        this.viewCount = post.getViewCount();
        this.commentsCount = post.getCommentsCount();
    }
}
