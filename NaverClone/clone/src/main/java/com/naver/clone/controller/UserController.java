package com.naver.clone.controller;

import com.naver.clone.dto.UserDto;
import com.naver.clone.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

@RestController // 1. 이 클래스가 JSON 데이터를 주고받는 API 컨트롤러임을 선언
@RequestMapping("/api/user") // 2. 모든 주소 앞에 "/api"를 붙임
public class UserController {

    private final UserMapper userMapper;
    private final UserDto UserDto;

    // 3. 생성자를 통해 Mapper를 주입받음 (DB 연결 통로 확보)
    public UserController(UserMapper userMapper, UserDto userDto) {
        this.userMapper = userMapper;
        this.UserDto = userDto;
    }

    @PostMapping("/login") // 보안을 위해 POST 방식을 사용
    public UserDto login(@RequestBody UserDto loginRequest) {
        UserDto user = userMapper.loginCheck(loginRequest);

        return user;
    }
}