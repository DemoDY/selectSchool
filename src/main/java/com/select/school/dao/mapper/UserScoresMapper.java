package com.select.school.dao.mapper;

import com.select.school.model.entity.UserScores;

public interface UserScoresMapper {


    /**
     * 批量插入
     * @param userScores
     * @return
     */
    int insertList(UserScores userScores);
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserScores selectUsername(String username);
}
