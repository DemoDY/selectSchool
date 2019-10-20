package com.select.school.mapper;

import com.select.school.model.entity.Manager;

public interface ManagerMapper {

    Manager findByLogin(String loginName);
}
