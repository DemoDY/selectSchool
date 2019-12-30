package com.select.school.mapper;

import com.select.school.model.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    public int create(Order pi);

    public int delete(Map<String, Object> paramMap);

    public int update(Map<String, Object> paramMap);

    public List<Order> query(Map<String, Object> paramMap);

    public Order detail(Map<String, Object> paramMap);

    public int count(Map<String, Object> paramMap);

    public List<Order> selectAll(Map<String, Object> paramMap);
}
