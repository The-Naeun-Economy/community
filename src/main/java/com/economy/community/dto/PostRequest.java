package com.economy.community.dto;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {
    @NotNull
    @Description("제목은 필수입니다.")
    public String title;
    @NotNull
    @Description("내용은 필수입니다.")
    public String content;
}
