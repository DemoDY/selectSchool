package com.select.school.mapper;

import com.select.school.model.entity.Weight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeightMapper {
    /**
     * 查询 权重列表 集合
     * @return
     */
    List<Weight> selectWeightList();

    /**
     * 查询 Ib Act 权重数据
     * @param ibActWeight
     * @return
     */
    List<Weight> selectIbActWeightDream(@Param("ibActWeight") double ibActWeight);
}
