package com.select.school.model.vo;

import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.Option;

import java.util.List;

public class OptionVo extends Option {
    private List<OptionDTO> optionList;

    public List<OptionDTO> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionDTO> optionList) {
        this.optionList = optionList;
    }
}
