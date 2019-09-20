package com.select.school.model.vo;

import com.select.school.model.entity.UserScores;

import java.util.List;

public class UserScoreVo {
    private List<UserScores> userScores;

    public List<UserScores> getUserScores() {
        return userScores;
    }

    public void setUserScores(List<UserScores> userScores) {
        this.userScores = userScores;
    }
}
