package com.select.school.mapper;

import com.select.school.model.dto.ProblemDTO;
import com.select.school.model.entity.Problem;

import java.util.List;
import java.util.Map;

public interface ProblemMapper {
    List<ProblemDTO> selectProblems();
    public List<Problem> selectAll(Map<String, Object> paramMap);
    public int count(Map<String, Object> paramMap);
}
