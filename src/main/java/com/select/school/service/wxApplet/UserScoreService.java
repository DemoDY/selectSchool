package com.select.school.service.wxApplet;

import com.select.school.utils.result.AjaxResult;
import com.select.school.model.dto.OptionDTO;

import java.util.List;

public interface UserScoreService {

    //插入数据
    public AjaxResult insertOption(List<OptionDTO> options,String openid,String version);
    //查询数据
    public AjaxResult selectSchool(int id);
}
