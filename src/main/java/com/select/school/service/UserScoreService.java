package com.select.school.service;

import com.select.school.bean.AjaxResult;
import com.select.school.model.dto.OptionDTO;

import java.util.List;

public interface UserScoreService {

    public AjaxResult selectOption(List<OptionDTO> options);
}
