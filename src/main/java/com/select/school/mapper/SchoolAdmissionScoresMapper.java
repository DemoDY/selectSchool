package com.select.school.mapper;

import com.select.school.model.entity.SchoolAdmissionScores;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SchoolAdmissionScoresMapper {
    //查询所有数据
    List<SchoolAdmissionScores> selectByAll();
    //根据分数 查询托福分数以下的数据
    List<SchoolAdmissionScores> selectByToeflLowReq(@Param("toeflLowReq") int toeflLowReq);
    //根据分数 查询雅思分数以下的数据
    List<SchoolAdmissionScores> selectByIeltsLowReq(@Param("ieltsLowReq")double ieltsLowReq);
}
