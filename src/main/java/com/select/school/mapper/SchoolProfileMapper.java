package com.select.school.mapper;

import com.select.school.model.entity.SchoolProfile;
import org.apache.ibatis.annotations.Param;

public interface SchoolProfileMapper {
    SchoolProfile selectById(@Param("id") int id);
}
