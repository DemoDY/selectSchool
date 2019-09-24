package com.select.school.mapper;

import com.select.school.model.entity.UserScores;

public interface UserScoresMapper {


    /**
     * 批量插入
     * @param userScores
     * @return
     */
    int insertList(UserScores userScores);
    /**
     * 根据openId查询
     * @param openId
     * @return
     */
    UserScores selectOpenId(String openId);
}
