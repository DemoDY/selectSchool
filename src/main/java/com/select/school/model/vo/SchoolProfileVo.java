package com.select.school.model.vo;

public class SchoolProfileVo {
    private String id;
    /**
     * 学校名称
     */
    private String schoolName;
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

    private String dreamSchool;

    public SchoolProfileVo(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDreamSchool() {
        return dreamSchool;
    }

    public void setDreamSchool(String dreamSchool) {
        this.dreamSchool = dreamSchool;
    }

    public SchoolProfileVo(String id, String schoolName, int schoolRank, String schoolProfile, String crest, String state, String theArea, String publicPrivate,String dreamSchool) {
        this.id = id;
        this.schoolName = schoolName;
        this.schoolRank = schoolRank;
        this.schoolProfile = schoolProfile;
        this.crest = crest;
        this.state = state;
        this.theArea = theArea;
        this.publicPrivate = publicPrivate;
        this.dreamSchool = dreamSchool;
    }

    @Override
    public String toString() {
        return "SchoolProfileVo{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", schoolRank=" + schoolRank +
                ", schoolProfile='" + schoolProfile + '\'' +
                ", crest='" + crest + '\'' +
                ", state='" + state + '\'' +
                ", theArea='" + theArea + '\'' +
                ", publicPrivate='" + publicPrivate + '\'' +
                ", dreamSchool='" + dreamSchool + '\'' +
                '}';
    }
}
