package com.select.school.dao.mapper;

import com.select.school.model.entity.SchoolProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolProfileMapper {
    SchoolProfile selectById(String id);
}
