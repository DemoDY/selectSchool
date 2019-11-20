package com.select.school.model.vo;

//雷达图字段
public class RadarMapVo {

    private int chGpaAvgStu;//中国学生平均GPA
    /**
     * 学校排名
     */
    private int schoolRank;
    private int chSatAvgHighStu;//中国学生SAT平均高位成绩
    private int actAvgResults;//ACT平均成绩
    private int toeflLowReq;//TOEFL 最低要求
    private int apWeight;//AP 选课数量
    private int ibAvgResults;//IB 平均成绩


    public int getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(int schoolRank) {
        this.schoolRank = schoolRank;
    }

    public int getActAvgResults() {
        return actAvgResults;
    }

    public void setActAvgResults(int actAvgResults) {
        this.actAvgResults = actAvgResults;
    }

    public int getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(int toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }


    public int getIbAvgResults() {
        return ibAvgResults;
    }

    public void setIbAvgResults(int ibAvgResults) {
        this.ibAvgResults = ibAvgResults;
    }

    public int getChGpaAvgStu() {
        return chGpaAvgStu;
    }

    public void setChGpaAvgStu(int chGpaAvgStu) {
        this.chGpaAvgStu = chGpaAvgStu;
    }

    public int getChSatAvgHighStu() {
        return chSatAvgHighStu;
    }

    public void setChSatAvgHighStu(int chSatAvgHighStu) {
        this.chSatAvgHighStu = chSatAvgHighStu;
    }

    public int getApWeight() {
        return apWeight;
    }

    public void setApWeight(int apWeight) {
        this.apWeight = apWeight;
    }
}

