package com.select.school.mapper;

import com.select.school.model.entity.Weight;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WeightMapper {
    /**
     * 查询 权重列表 集合
     * @return
     */
    List<Weight> selectWeightList();

    /**
     * 查询 Ib Act 权重数据
     * @param
     * @return
     */
    List<Weight> selectIbActWeightDream(Map<String,Object> map);

    /**
     * 根据分数id 查询权重
     */
    Weight selectByScoreId(int scoreId);    /**
     * 根据id 查询权重
     */
    Weight selectById(int id);
}
