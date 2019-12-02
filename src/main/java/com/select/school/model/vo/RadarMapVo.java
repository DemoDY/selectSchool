package com.select.school.model.vo;

//雷达图字段
public class RadarMapVo {

//    private int chGpaAvgStu;//中国学生平均GPA
//    /**
//     * 学校排名
//     */
//    private int schoolRank;
//    private int chSatAvgHighStu;//中国学生SAT平均高位成绩
//    private int actAvgResults;//ACT平均成绩
//    private int toeflLowReq;//TOEFL 最低要求
//    private int apWeight;//AP 选课数量
//    private int ibAvgResults;//IB 平均成绩

    private String name;
    private String schoolMap;
    private String selfMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolMap() {
        return schoolMap;
    }

    public void setSchoolMap(String schoolMap) {
        this.schoolMap = schoolMap;
    }

    public String getSelfMap() {
        return selfMap;
    }

    public void setSelfMap(String selfMap) {
        this.selfMap = selfMap;
    }
}

