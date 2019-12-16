package com.select.school.model.dto;

import com.select.school.model.vo.RadarMapVo;

import java.util.List;

/**
 * 保底学校
 */
public class SafetySchoolDTO {
    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 中文名称
     */
    private String chName;
    /**
     * 学校简介
     */
    private  String schoolProfile;
    /**
     * 校徽
     */
    private String crest;
    private String nineteen;//2019 录取人数
    private String twenty;//2020 录取人数
    private String nationalStuAccep;//国际生录取率
    private String tuitionFees;//学费（美元）
    private String numNationalFreshmen;//大一国际生人数
    private String safetySchool;//保底学校
    private List<RadarMapVo> radarMap;//雷达图
    private String details;//报告关于学校的详情
    private List<SchoolDetails> schoolDetails;//学习数据详情

    public List<SchoolDetails> getSchoolDetails() {
        return schoolDetails;
    }

    public void setSchoolDetails(List<SchoolDetails> schoolDetails) {
        this.schoolDetails = schoolDetails;
    }

    public List<RadarMapVo> getRadarMap() {
        return radarMap;
    }

    public void setRadarMap(List<RadarMapVo> radarMap) {
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

    public String getSafetySchool() {
        return safetySchool;
    }

    public void setSafetySchool(String safetySchool) {
        this.safetySchool = safetySchool;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCrest() {
        return crest;
    }

    public void setCrest(String crest) {
        this.crest = crest;
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
}
