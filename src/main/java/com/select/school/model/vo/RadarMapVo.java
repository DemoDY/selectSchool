package com.select.school.model.vo;

//雷达图字段
public class RadarMapVo {

    private String chGpaAvgStu;//中国学生平均GPA
    /**
     * 学校排名
     */
    private String schoolRank;
    private String chSatAvgHighStu;//中国学生SAT平均高位成绩
    private String actAvgResults;//ACT平均成绩
    private String toeflLowReq;//TOEFL 最低要求
    private String apNumCourse;//AP 选课数量
    private String ibAvgResults;//IB 平均成绩

    public String getChGpaAvgStu() {
        return chGpaAvgStu;
    }

    public void setChGpaAvgStu(String chGpaAvgStu) {
        this.chGpaAvgStu = chGpaAvgStu;
    }

    public String getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(String schoolRank) {
        this.schoolRank = schoolRank;
    }

    public String getChSatAvgHighStu() {
        return chSatAvgHighStu;
    }

    public void setChSatAvgHighStu(String chSatAvgHighStu) {
        this.chSatAvgHighStu = chSatAvgHighStu;
    }

    public String getActAvgResults() {
        return actAvgResults;
    }

    public void setActAvgResults(String actAvgResults) {
        this.actAvgResults = actAvgResults;
    }

    public String getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(String toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }

    public String getApNumCourse() {
        return apNumCourse;
    }

    public void setApNumCourse(String apNumCourse) {
        this.apNumCourse = apNumCourse;
    }

    public String getIbAvgResults() {
        return ibAvgResults;
    }

    public void setIbAvgResults(String ibAvgResults) {
        this.ibAvgResults = ibAvgResults;
    }
}

