package com.select.school.mapper;

import com.select.school.model.entity.Weight;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WeightMapper {
    /**
     * 查询 Ib Act 权重数据
     * @param
     * @return
     */
    List<Weight> selectIbActWeightDream(Map<String,Object> map);

    /**
     * 根据id 查询权重
     */
    Weight selectById(int id);
}
