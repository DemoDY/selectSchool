package com.select.school.mapper;

import com.select.school.model.entity.Order;
import com.select.school.model.entity.User;
import com.select.school.model.entity.WxAffirm;

import java.util.List;
import java.util.Map;

public interface WxAffirmMapper {
    public int create(WxAffirm pi);

    public int delete(Map<String, Object> paramMap);

    public int update(Map<String, Object> paramMap);

    public List<WxAffirm> query(Map<String, Object> paramMap);

    public WxAffirm detail(Map<String, Object> paramMap);

    public int count(Map<String, Object> paramMap);

    public List<WxAffirm> selectAll(Map<String, Object> paramMap);
}
