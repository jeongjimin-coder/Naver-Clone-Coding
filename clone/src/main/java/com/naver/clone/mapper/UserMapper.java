package com.naver.clone.mapper;

import com.naver.clone.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDto getUserById(String id);
    UserDto loginCheck(UserDto loginRequest);
}
