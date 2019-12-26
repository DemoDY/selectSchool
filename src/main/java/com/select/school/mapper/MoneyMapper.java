package com.select.school.mapper;

import com.select.school.model.entity.Money;

import java.util.Map;

public interface MoneyMapper {
    Money findByState(int state);
}
