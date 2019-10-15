package com.select.school.model.entity;

/**
 * 用户答案表
 */
public class Scores {
    private int id;
    private int userId;
    private String gpaAvg;//中国学生平均GPA
    /**
     * 学校排名
     */
    private String rank; //排名
    private String satAvgHigh;//中国学生SAT平均高位成绩
    private String actAvg;//ACT平均成绩
    private String toeflLow;//TOEFL 最低要求
    private String apCourse;//AP 选课数量
    private String ibAvg;//IB 平均成绩

    public Scores(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGpaAvg() {
        return gpaAvg;
    }

    public void setGpaAvg(String gpaAvg) {
        this.gpaAvg = gpaAvg;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSatAvgHigh() {
        return satAvgHigh;
    }

    public void setSatAvgHigh(String satAvgHigh) {
        this.satAvgHigh = satAvgHigh;
    }

    public String getActAvg() {
        return actAvg;
    }

    public void setActAvg(String actAvg) {
        this.actAvg = actAvg;
    }

    public String getToeflLow() {
        return toeflLow;
    }

    public void setToeflLow(String toeflLow) {
        this.toeflLow = toeflLow;
    }

    public String getApCourse() {
        return apCourse;
    }

    public void setApCourse(String apCourse) {
        this.apCourse = apCourse;
    }

    public String getIbAvg() {
        return ibAvg;
    }

    public void setIbAvg(String ibAvg) {
        this.ibAvg = ibAvg;
    }
}
