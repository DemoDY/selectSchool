package com.select.school.mapper;

import com.select.school.model.entity.Money;

public interface MoneyMapper {
    Money findByState(int state);
}
