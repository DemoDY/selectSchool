package com.select.school.dao.mapper;

import com.select.school.model.dto.ProblemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper {

    List<ProblemDTO> selectProblems();

}
