package com.select.school.dao.mapper;

import com.select.school.model.entity.Weight;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeightMapper {
    /**
     * 查询 权重列表 集合
     * @return
     */
    List<Weight> selectWeightList();

    /**
     * 查询 Ib Act 权重数据
     * @param IbActWeight
     * @return
     */
    Weight selectIbActWeightDream(int IbActWeight);
}
