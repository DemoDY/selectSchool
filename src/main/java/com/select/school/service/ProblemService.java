package com.select.school.service;

import com.select.school.bean.AjaxResult;
import com.select.school.model.entity.UserScores;

import java.util.List;


public interface ProblemService {
    AjaxResult selectProblem();
    public int insertList(UserScores userScoresList);
}

