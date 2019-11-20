package com.select.school.mapper;

import com.select.school.model.entity.SchoolAdmissionScores;

import java.util.List;
import java.util.Map;

public interface SchoolAdmissionScoresMapper {
    SchoolAdmissionScores selectById(int schoolId);
    SchoolAdmissionScores selectId(int id);

    /**
     * 查询 Ib Act 权重数据
     * @param
     * @return
     */
    List<SchoolAdmissionScores> selectIbActWeightDream(Map<String,Object> map);
}
