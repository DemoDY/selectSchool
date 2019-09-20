package com.select.school.dao.mapper;

import com.select.school.model.entity.UserScores;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserScoresMapper {

    /**
     * 批量插入
     * @param userScores
     * @return
     */
    int insertList(@Param(value = "userScores") UserScores userScores);

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserScores selectUsername(String username);
}
