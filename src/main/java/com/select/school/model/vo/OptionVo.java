package com.select.school.model.vo;

import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.Option;

import java.util.List;

public class OptionVo extends Option {
    private List<OptionDTO> optionList;
    private String openid;
    private String version;
    public List<OptionDTO> getOptionList() {
        return optionList;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setOptionList(List<OptionDTO> optionList) {
        this.optionList = optionList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
