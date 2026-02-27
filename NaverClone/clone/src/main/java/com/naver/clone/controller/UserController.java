package com.naver.clone.controller;

import com.naver.clone.dto.UserDto;
import com.naver.clone.mapper.UserMapper;
import com.naver.clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.Map;
import java.util.Random;

@RestController // 1. 이 클래스가 JSON 데이터를 주고받는 API 컨트롤러임을 선언
@RequestMapping("/api/user") // 2. 모든 주소 앞에 "/api"를 붙임
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    // 3. 생성자를 통해 Mapper를 주입받음 (DB 연결 통로 확보)
    public UserController(UserMapper userMapper, UserService userService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/login") // 보안을 위해 POST 방식을 사용
    public UserDto login(@RequestBody UserDto loginRequest, HttpServletRequest request) {

        UserDto user = userMapper.findById(loginRequest.getUserId());

        if (user != null && passwordEncoder.matches(loginRequest.getUserPw(), user.getUserPw())) {
            // 2. 세션 생성 및 정보 저장
            HttpSession session = request.getSession(); // 세션 가져오기 (없으면 생성)
            session.setAttribute("LoginUser", user); // 세션에 "LoginUser" 라는 이름으로 유저 객체 생성
            session.setMaxInactiveInterval(30 * 60); // 30분간 유지
            return user;
        }
        return null;
    }

    @GetMapping
    public UserDto checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 마라

        if (session != null) {
            return (UserDto) session.getAttribute("LoginUser"); // 저장된 유저 정보 반환
        }
        return null;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화 (모든 데이터 삭제)
        }
    }

    @PostMapping("/join")
    public String join(@RequestBody UserDto userDto) {
        try {
            userService.join(userDto);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }


    }

    @PostMapping("/verify-id")
    public String verifyUserId(@RequestBody Map<String, String> request) {
        String UserId = request.get("userId");
        return userService.verifyId(UserId);
    }

    @PostMapping("/reset-pw")
    public String resetPw(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String newPw = request.get("newPw");
        return userService.updatePassword(userId, newPw);
    }

    @PostMapping("/find-id")
    public String findUserId(@RequestBody Map<String, String> request) {
        String userInfo = request.get("info");
        return userService.getUserIdByInfo(userInfo);
    }

    @PostMapping("/verify-send")
    public String sendVerifyCode(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String phone = request.get("phone");

        // 1. 4자리 랜덤 인증번호 생성
        Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));

        HttpSession session = httpRequest.getSession();
        session.setAttribute("verifyCode", code);

        // 2. 실제로는 여기서 SMS API를 호출하지만, 우리는 콘솔에 출력!
        System.out.println("========================================");
        System.out.println("검증 세션 ID: " + session.getId());
        System.out.println("휴대폰 번호: " + phone);
        System.out.println("생성된 인증번호: " + code);
        System.out.println("========================================");

        // 3. 나중에 검증을 위해 세션에 저장하거나 DB에 잠깐 담아둘 수도 있습니다

        return "success"; // 프론트로 성공 신호를 보냄
    }

    @PostMapping("/verify-check")
    public boolean verifyCheck(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String userInputCode = request.get("code");
        System.out.println("입력한 코드: " + userInputCode);
        HttpSession session = httpRequest.getSession(false); // 기존 세션이 있는지 확인

        // 4. 세션에서 아까 만든 번호 꺼내기
        if (session == null) return false;


        String serverCode = (String) session.getAttribute("verifyCode");
        System.out.println("발송 세션 ID: " + session.getId());
        System.out.println("서버에 저장된 번호: " + serverCode);
        System.out.println("사용자가 입력한 번호: " + userInputCode);

        // 5. 서버 번호와 사용자 입력 번호 비교
        return serverCode != null && serverCode.equals(userInputCode);
    }


}