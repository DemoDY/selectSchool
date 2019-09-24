package com.select.school.service.wxApplet;

import com.select.school.utils.result.AjaxResult;
import com.select.school.model.dto.OptionDTO;

import java.util.List;

public interface UserScoreService {

    public AjaxResult selectOption(List<OptionDTO> options);
}
