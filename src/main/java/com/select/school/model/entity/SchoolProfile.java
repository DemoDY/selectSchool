package com.select.school.model.entity;

public class SchoolProfile {
    private int id;
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
    private int schoolRank;
    /**
     * 学校简介
     */
    private  String schoolProfile;
    /**
     * 校徽
     */
    private String crest;
    /**
     * 所在州
     */
    private String state;
    /**
     * 所在地区
     */
    private String theArea;
    /**
     * 公立 public / 私立 Private
     */
    private String publicPrivate;

    public SchoolProfile(int id, String schoolName, String chName, int schoolRank, String schoolProfile, String crest, String state, String theArea, String publicPrivate) {
        this.id = id;
        this.schoolName = schoolName;
        this.chName = chName;
        this.schoolRank = schoolRank;
        this.schoolProfile = schoolProfile;
        this.crest = crest;
        this.state = state;
        this.theArea = theArea;
        this.publicPrivate = publicPrivate;
    }

    public SchoolProfile(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(int schoolRank) {
        this.schoolRank = schoolRank;
    }

    public String getSchoolProfile() {
        return schoolProfile;
    }

    public void setSchoolProfile(String schoolProfile) {
        this.schoolProfile = schoolProfile;
    }

    public String getCrest() {
        return crest;
    }

    public void setCrest(String crest) {
        this.crest = crest;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTheArea() {
        return theArea;
    }

    public void setTheArea(String theArea) {
        this.theArea = theArea;
    }

    public String getPublicPrivate() {
        return publicPrivate;
    }

    public void setPublicPrivate(String publicPrivate) {
        this.publicPrivate = publicPrivate;
    }

    public SchoolProfile(int id, String schoolName, int schoolRank, String schoolProfile, String crest, String state, String theArea, String publicPrivate) {
        this.id = id;
        this.schoolName = schoolName;
        this.schoolRank = schoolRank;
        this.schoolProfile = schoolProfile;
        this.crest = crest;
        this.state = state;
        this.theArea = theArea;
        this.publicPrivate = publicPrivate;
    }

    @Override
    public String toString() {
        return "SchoolProfile{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", schoolRank=" + schoolRank +
                ", schoolProfile='" + schoolProfile + '\'' +
                ", crest='" + crest + '\'' +
                ", state='" + state + '\'' +
                ", theArea='" + theArea + '\'' +
                ", publicPrivate='" + publicPrivate + '\'' +
                '}';
    }
}
