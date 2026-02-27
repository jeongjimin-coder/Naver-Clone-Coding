package com.naver.clone.service;

import com.naver.clone.dto.UserDto;
import com.naver.clone.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // 둘 중 하나라도 실패하면 전체 롤백
    public void join(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getUserPw());
        userDto.setUserPw(encodedPassword);

        userMapper.insertAccount(userDto);
        userMapper.insertProfile(userDto);
    }

    public String getUserIdByInfo(String info) {
        String result = userMapper.selectById(info);
        if (result == null) return "";
        return result;

    }

    public String verifyId(String userId) {
        int count = userMapper.checkUserExists(userId);
        return count > 0 ? "exist" : "not_found";
    }

    @Transactional  // 보안 작업에는 트랜잭션 필수
    public String updatePassword(String userId, String newPw) {

        String encodedPassword = passwordEncoder.encode(newPw);

        int result = userMapper.updatePassword(userId, encodedPassword);
        return result > 0 ? "success" : "fail";
    }
}
