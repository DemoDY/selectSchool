package com.select.school.mapper;

import com.select.school.model.entity.SchoolProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SchoolProfileMapper {
    SchoolProfile selectById(@Param("id") int id);
    public List<SchoolProfile> selectAll(Map<String, Object> paramMap);
    public int count(Map<String, Object> paramMap);

    SchoolProfile chName(@Param("chName") String chName);
}
