package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String category;
}
