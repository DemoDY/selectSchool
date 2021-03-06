package com.select.school.model.dto;

import com.select.school.model.entity.Option;

import java.util.List;

public class ProblemDTO {

    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 标题说明
     */
    private String description;
    private int number;
    /**
     * 单选 (radio) or 多选(checkbox)
     * @return
     */
    private String radioCheckBox;

    private String skipUp;//向上跳转

    private List<Option> optionList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public String getRadioCheckBox() {
        return radioCheckBox;
    }

    public void setRadioCheckBox(String radioCheckBox) {
        this.radioCheckBox = radioCheckBox;
    }

    public String getSkipUp() {
        return skipUp;
    }

    public void setSkipUp(String skipUp) {
        this.skipUp = skipUp;
    }
}
