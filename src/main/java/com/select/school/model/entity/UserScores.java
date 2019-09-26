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
    private String tl;
    private Date createTime;

    private String actSat; //act或者sat
    private String apIb;//ap或者ib

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

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
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
}
