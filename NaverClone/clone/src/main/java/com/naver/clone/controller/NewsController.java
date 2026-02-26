package com.naver.clone.controller;

import com.naver.clone.mapper.NewsMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsMapper newsMapper;

    public NewsController(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @GetMapping("/list")
    public List<Map<String, Object>> getNewsList() {
        return newsMapper.getRandomNews();
    }
}
