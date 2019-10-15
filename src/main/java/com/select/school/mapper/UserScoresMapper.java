package com.select.school.mapper;

import com.select.school.model.entity.UserScores;
import com.select.school.model.vo.UserScoreVo;

public interface UserScoresMapper {


    /**
     * 批量插入
     * @param userScores
     * @return
     */
    int insertList(UserScores userScores);
    /**
     * 根据openId查询
     * @param id
     * @return
     */
    UserScores selectOpenId(int id);
    UserScoreVo selectId(int id);
}
