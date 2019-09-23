package com.select.school.model.entity;

import java.util.Date;

public class UserScores {
    private String id;
    private String username;
    private String openId;
    //得分
    private int scores;
    /**
     * 雅思或者托福
     */
    private String tl;
    private Date createTime;

    private String actSat; //act或者sat
    private String apIb;//ap或者ib

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
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
}
