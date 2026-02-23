package com.naver.clone.controller;

import com.naver.clone.dto.UserDto;
import com.naver.clone.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController // 1. 이 클래스가 JSON 데이터를 주고받는 API 컨트롤러임을 선언
@RequestMapping("/api/user") // 2. 모든 주소 앞에 "/api"를 붙임
public class UserController {

    private final UserMapper userMapper;

    // 3. 생성자를 통해 Mapper를 주입받음 (DB 연결 통로 확보)
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/login") // 보안을 위해 POST 방식을 사용
    public UserDto login(@RequestBody UserDto loginRequest, HttpServletRequest request) {

        UserDto user = userMapper.loginCheck(loginRequest);

        if (user != null) {
            // 2. 세션 생성 및 정보 저장
            HttpSession session = request.getSession(); // 세션 가져오기 (없으면 생성)
            session.setAttribute("LoginUser", user); // 세션에 "LoginUser" 라는 이름으로 유저 객체 생성
            session.setMaxInactiveInterval(30 * 60); // 30분간 유지
        }
        return user;
    }

    public UserDto checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 마라

        if (session != null) {
            return (UserDto) session.getAttribute("LoginUser"); // 저장된 유저 정보 반환
        }
        return null;
    }
}