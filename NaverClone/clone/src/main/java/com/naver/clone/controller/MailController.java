package com.naver.clone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {

    @GetMapping("/list")
    public List<String> getMailList() {
        System.out.println("요청이 들어옴");
        return Arrays.asList(
                "새로운 환경에서 로그인되었습니다.",
                "네이버 메일"
        );
    }
}
