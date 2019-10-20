package com.select.school.mapper;

import com.select.school.model.entity.Scores;

public interface ScoresMapper {
    int insertScores(Scores scores);
    Scores findByUserScoreId(int userScoreId);
}
