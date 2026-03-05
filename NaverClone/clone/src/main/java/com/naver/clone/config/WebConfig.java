package com.naver.clone.config;

import com.naver.clone.service.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 🚀 allowedOrigins 대신 Pattern을 써야 Credentials 허용이 잘 됩니다.
                .allowedOriginPatterns("http://localhost:5173")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie"); // 🚀 브라우저가 쿠키를 볼 수 있게 노출
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 부모인 registry를 받아서
        registry.addInterceptor(loginInterceptor)   // 여기에 등록을 하고
                .addPathPatterns("/**") // 범위 지정
                .excludePathPatterns("/", "/error", "/login", "/api/**", "/css/**", "/js/**"); // 제외할 곳을 정함


    }
}
