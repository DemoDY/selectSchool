package com.select.school.service.wxApplet;

import com.select.school.model.vo.SchoolProfileVo;
import com.select.school.utils.result.AjaxResult;
import com.select.school.model.dto.OptionDTO;

import java.util.List;

public interface UserScoreService {

    //报错数据
    public AjaxResult insertOption(List<OptionDTO> options);
    //查询数据
    public AjaxResult selectSchool(String openId);
}
