package com.select.school.model.entity;

import java.util.Date;

public class UserScores {
    private int id;
    private String openId;
    //得分
    private int scores;
    /**
     * 雅思或者托福 分数
     */
    private String tlScore;
    private int tl;//2 代表托福  1 代表雅思
    private Date createTime;

    private String actSat; //act或者sat
    private String apIb;//ap或者ib

    private int tuitionFees;//接受 学费的范围 例：五万美金以下

    private int required;// 每年入学大一学生数

    private String sixQuestion;// 第六题 问题

    private String noSat;//第十三题不需要sat成绩 字段为0

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }

    public String getActSat() {
        return actSat;
    }

    public void setActSat(String actSat) {
        this.actSat = actSat;
    }

    public String getApIb() {
        return apIb;
    }

    public void setApIb(String apIb) {
        this.apIb = apIb;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getTlScore() {
        return tlScore;
    }

    public void setTlScore(String tlScore) {
        this.tlScore = tlScore;
    }

    public int getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(int tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getSixQuestion() {
        return sixQuestion;
    }

    public void setSixQuestion(String sixQuestion) {
        this.sixQuestion = sixQuestion;
    }

    public String getNoSat() {
        return noSat;
    }

    public void setNoSat(String noSat) {
        this.noSat = noSat;
    }

    @Override
    public String toString() {
        return "UserScores{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", scores=" + scores +
                ", tlScore='" + tlScore + '\'' +
                ", tl='" + tl + '\'' +
                ", createTime=" + createTime +
                ", actSat='" + actSat + '\'' +
                ", apIb='" + apIb + '\'' +
                ", tuitionFees='" + tuitionFees + '\'' +
                ", required='" + required + '\'' +
                ", sixQuestion='" + sixQuestion + '\'' +
                ", noSat='" + noSat + '\'' +
                '}';
    }
}
