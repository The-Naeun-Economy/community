package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeletePostRequest {
    @NotNull
    private Long id;
}
