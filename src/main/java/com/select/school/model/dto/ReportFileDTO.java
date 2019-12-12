package com.select.school.model.dto;


import com.select.school.model.vo.AcceptanceVo;
import com.select.school.model.vo.SchoolProfileVo;

import java.util.List;

/**
 * 生成报告文档
 */
public class ReportFileDTO {

    private String preface;//前言
    private String dataModel;//数据模型

    private SchoolProfileVo schoolProfileVos;//学校

//    private AcceptanceVo acceptance;//录取率

    private String question;

    private String explain;

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public String getDataModel() {
        return dataModel;
    }

    public void setDataModel(String dataModel) {
        this.dataModel = dataModel;
    }

    public SchoolProfileVo getSchoolProfileVos() {
        return schoolProfileVos;
    }

    public void setSchoolProfileVos(SchoolProfileVo schoolProfileVos) {
        this.schoolProfileVos = schoolProfileVos;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
