package com.select.school.model.vo;

public class SchoolProfileVo {
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
//    private String crest;
    /**
     * 所在州
     */
//    private String state;
    /**
     * 所在地区
     */
//    private String theArea;
    /**
     * 公立 public / 私立 Private
     */
//    private String publicPrivate;

    private String dreamSchool;//梦想学校

    public SchoolProfileVo(){
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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



    public String getDreamSchool() {
        return dreamSchool;
    }

    public void setDreamSchool(String dreamSchool) {
        this.dreamSchool = dreamSchool;
    }

    public SchoolProfileVo(int id, String schoolName, int schoolRank, String schoolProfile,String dreamSchool) {
        this.id = id;
        this.schoolName = schoolName;
        this.schoolRank = schoolRank;
        this.schoolProfile = schoolProfile;
        this.dreamSchool = dreamSchool;
    }

    @Override
    public String toString() {
        return "SchoolProfileVo{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", schoolRank=" + schoolRank +
                ", schoolProfile='" + schoolProfile + '\'' +
                ", dreamSchool='" + dreamSchool + '\'' +
                '}';
    }
}
