package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String userId;

    @NotNull
    private String userNickname;

    @NotNull
    private String category;
}
