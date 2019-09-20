package com.select.school.model.dto;

import com.select.school.model.entity.Option;

import java.util.List;

public class ProblemDTO {

    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 标题说明
     */
    private String description;

    private List<OptionDTO> optionList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OptionDTO> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionDTO> optionList) {
        this.optionList = optionList;
    }
}
