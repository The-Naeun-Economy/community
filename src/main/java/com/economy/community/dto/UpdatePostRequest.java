package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdatePostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;
}
