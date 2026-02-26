package com.naver.clone.dto;

import lombok.Data; // 이게 있으면 Getter; Setter를 자동으로 생성

@Data
public class UserDto {
    // 1. USER_ACCOUNT 테이블
    private String userId;
    private String userPw;
    private String status;
    private String createDate;

    // 2. USER_PROFILE 테이블
    private String userName;
    private String email;
    private String birth;
    private String telecom;
    private String gender;
    private String isLocal;
    private String phone;
}
