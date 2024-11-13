package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;
}
