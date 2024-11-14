package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;
}
