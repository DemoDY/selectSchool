package com.select.school.mapper;

import com.select.school.model.dto.ProblemDTO;

import java.util.List;

public interface ProblemMapper {
    List<ProblemDTO> selectProblems();

}
