package com.select.school.dao.mapper;

import com.select.school.model.entity.Option;
import org.mapstruct.Mapper;

@Mapper
public interface OptionMapper {

    Option selectByOption(String option);
}
