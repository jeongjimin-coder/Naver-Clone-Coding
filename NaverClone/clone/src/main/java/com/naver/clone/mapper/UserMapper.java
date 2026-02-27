package com.naver.clone.mapper;

import com.naver.clone.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDto findById(String userId);

    int insertAccount(UserDto userDto);
    int insertProfile(UserDto userDto);
    String selectById(String info);
    int checkUserExists(String userId);
    int updatePassword(String userId, String newPw);

}
