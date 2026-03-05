package com.naver.clone.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 세션에서 로그인된 유저 정보 확인
        HttpSession session = request.getSession();
        System.out.println("세션 정보: " + session);
        System.out.println("요청 온 주소: " + request.getRequestURI());
        Object loginUser = session.getAttribute("loginUser");
        System.out.println("세션 확인 결과: " + loginUser);

        // 2. 로그인 정보가 없다면 가로채기
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // 401 에러 코드
            return false;
        }

        return true; // 로그인 상태면 통과
    }
}
