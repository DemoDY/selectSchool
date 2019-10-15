package com.select.school.model.dto;

import com.select.school.model.vo.RadarMapVo;

import java.util.List;

/**
 * 梦想学校
 */
public class DreamSchoolDTO {
    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 中文名称
     */
    private String chName;
    /**
     * 学校排名
     */
    private String schoolRank;
    /**
     * 学校简介
     */
    private String schoolProfile;
    /**
     * 校徽
     */
//    private String crest;
    private String nineteen;//2019 录取人数
    private String twenty;//2020 录取人数
    private String nationalStuAccep;//国际生录取率
    private String tuitionFees;//学费（美元）
    private String numNationalFreshmen;//大一国际生人数
    private String dreamSchool;//梦想学校

    private RadarMapVo radarMap;//雷达图

    private String details;//报告关于学校的详情

    public RadarMapVo getRadarMap() {
        return radarMap;
    }

    public void setRadarMap(RadarMapVo radarMap) {
        this.radarMap = radarMap;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getSchoolProfile() {
        return schoolProfile;
    }

    public void setSchoolProfile(String schoolProfile) {
        this.schoolProfile = schoolProfile;
    }

    public String getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(String schoolRank) {
        this.schoolRank = schoolRank;
    }

    public String getNineteen() {
        return nineteen;
    }

    public void setNineteen(String nineteen) {
        this.nineteen = nineteen;
    }

    public String getTwenty() {
        return twenty;
    }

    public void setTwenty(String twenty) {
        this.twenty = twenty;
    }

    public String getNationalStuAccep() {
        return nationalStuAccep;
    }

    public void setNationalStuAccep(String nationalStuAccep) {
        this.nationalStuAccep = nationalStuAccep;
    }

    public String getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(String tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    public String getNumNationalFreshmen() {
        return numNationalFreshmen;
    }

    public void setNumNationalFreshmen(String numNationalFreshmen) {
        this.numNationalFreshmen = numNationalFreshmen;
    }

    public String getDreamSchool() {
        return dreamSchool;
    }

    public void setDreamSchool(String dreamSchool) {
        this.dreamSchool = dreamSchool;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
