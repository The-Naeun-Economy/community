package com.economy.community.dto;

import lombok.Data;

@Data
public class UpdatePostUserNickName {
    private Long userId;
    private String nickName;
}
