package com.naver.clone.dto;

import lombok.Data; // 이게 있으면 Getter; Setter를 자동으로 생성

@Data
public class UserDto {
    private String userId;
    private String userPw;
    private String userName;
    private String email;
}
