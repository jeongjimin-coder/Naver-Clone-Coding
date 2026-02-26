package com.naver.clone.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface NewsMapper {

    @Select("SELECT * FROM (SELECT * FROM NEWS_STAND ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 24")
    List<Map<String, Object>> getRandomNews();
}
