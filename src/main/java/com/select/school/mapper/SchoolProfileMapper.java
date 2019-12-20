package com.select.school.mapper;

import com.select.school.model.dto.SchoolDTO;
import com.select.school.model.entity.SchoolProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SchoolProfileMapper {
    SchoolProfile selectById(@Param("id") int id);
    int update(SchoolDTO schoolDTO);
    public List<SchoolProfile> selectAll(Map<String, Object> paramMap);
    public List<SchoolProfile> selectSchool();
    public int count(Map<String, Object> paramMap);

    int insertSchool(SchoolProfile schoolProfile);
}
