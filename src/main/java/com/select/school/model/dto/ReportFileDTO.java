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

    private List<SchoolProfileVo> schoolProfileVos;//学校

    private List<AcceptanceVo> acceptance;//录取率

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

    public List<SchoolProfileVo> getSchoolProfileVos() {
        return schoolProfileVos;
    }

    public void setSchoolProfileVos(List<SchoolProfileVo> schoolProfileVos) {
        this.schoolProfileVos = schoolProfileVos;
    }

    public List<AcceptanceVo> getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(List<AcceptanceVo> acceptance) {
        this.acceptance = acceptance;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
