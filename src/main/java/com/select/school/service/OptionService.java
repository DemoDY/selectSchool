package com.select.school.service;

import com.select.school.bean.AjaxResult;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.Option;

import java.util.List;

public interface OptionService {

    public AjaxResult selectOption(List<OptionDTO> options);
}
