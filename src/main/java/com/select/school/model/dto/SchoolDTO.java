/**
 * projectName: selectSchool
 * fileName: SchoolDTO.java
 * packageName: com.select.school.model.dto
 * date: 2019-12-17 16:51
 * copyright(c) 德慧
 */
package com.select.school.model.dto;

public class SchoolDTO {
    private int schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 中文名称
     */
    private String chName;
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
    private int nineteen;//2019 录取排名
    private int twenty;//2020 录取排名
    private int firstStudentsNum;//留学生大一入学人数
    private String comAdmissionRate;//2019 综合录取率
    private String numberFreshmen;//大一新生人数
    private String nationalStuAccep;//国际生录取率
    private String tuitionFees;//学费（美元）
    private String numNationalFreshmen;//大一国际生人数
    private String chStuHighRank;//中国录取学生高中年级排名
    private String chStuWeightRank;//中国学生排名权重
    private String chGpaAvgStu;//中国学生平均GPA
    private String chGpaWeightStu;//中国学生GPA权重
    private String chSatAvgLowStu;//中国学生SAT平均低位成绩
    private String chSatAvgHighStu;//中国学生SAT平均高位成绩
    private String noSat;//哪些学校不需要sat
    private int satWeight;//SAT权重
    private int actAvgResults;//ACT平均成绩
    private int actWeight;//ACT权重
    private int apWeight;//AP 选课权重
    private int ibAvgResults;//IB 平均成绩
    private int ibWeight;//IB 权重
    private int toeflLowReq;//TOEFL 最低要求
    private String examAp;//选考ap
    private double ieltsLowReq;//IELTS 最低要求
    private int topWeight;// top权重
    private int activitiesWeight;// 课外活动权重
    private int competitionWeight;// 竞赛权重
    private int welfareWeight;// 公益权重

    public SchoolDTO(){}
    public SchoolDTO(int schoolId,String schoolName, String chName, String schoolProfile, String crest, String state, String theArea, String publicPrivate, int nineteen, int twenty, int firstStudentsNum, String comAdmissionRate, String numberFreshmen, String nationalStuAccep, String tuitionFees, String numNationalFreshmen, String chStuHighRank, String chStuWeightRank, String chGpaAvgStu, String chGpaWeightStu, String chSatAvgLowStu, String chSatAvgHighStu, String noSat, int satWeight, int actAvgResults, int actWeight, int apWeight, int ibAvgResults, int ibWeight, int toeflLowReq, String examAp, double ieltsLowReq, int topWeight, int activitiesWeight, int competitionWeight, int welfareWeight) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.chName = chName;
        this.schoolProfile = schoolProfile;
        this.crest = crest;
        this.state = state;
        this.theArea = theArea;
        this.publicPrivate = publicPrivate;
        this.nineteen = nineteen;
        this.twenty = twenty;
        this.firstStudentsNum = firstStudentsNum;
        this.comAdmissionRate = comAdmissionRate;
        this.numberFreshmen = numberFreshmen;
        this.nationalStuAccep = nationalStuAccep;
        this.tuitionFees = tuitionFees;
        this.numNationalFreshmen = numNationalFreshmen;
        this.chStuHighRank = chStuHighRank;
        this.chStuWeightRank = chStuWeightRank;
        this.chGpaAvgStu = chGpaAvgStu;
        this.chGpaWeightStu = chGpaWeightStu;
        this.chSatAvgLowStu = chSatAvgLowStu;
        this.chSatAvgHighStu = chSatAvgHighStu;
        this.noSat = noSat;
        this.satWeight = satWeight;
        this.actAvgResults = actAvgResults;
        this.actWeight = actWeight;
        this.apWeight = apWeight;
        this.ibAvgResults = ibAvgResults;
        this.ibWeight = ibWeight;
        this.toeflLowReq = toeflLowReq;
        this.examAp = examAp;
        this.ieltsLowReq = ieltsLowReq;
        this.topWeight = topWeight;
        this.activitiesWeight = activitiesWeight;
        this.competitionWeight = competitionWeight;
        this.welfareWeight = welfareWeight;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
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

    public int getNineteen() {
        return nineteen;
    }

    public void setNineteen(int nineteen) {
        this.nineteen = nineteen;
    }

    public int getTwenty() {
        return twenty;
    }

    public void setTwenty(int twenty) {
        this.twenty = twenty;
    }

    public int getFirstStudentsNum() {
        return firstStudentsNum;
    }

    public void setFirstStudentsNum(int firstStudentsNum) {
        this.firstStudentsNum = firstStudentsNum;
    }

    public String getComAdmissionRate() {
        return comAdmissionRate;
    }

    public void setComAdmissionRate(String comAdmissionRate) {
        this.comAdmissionRate = comAdmissionRate;
    }

    public String getNumberFreshmen() {
        return numberFreshmen;
    }

    public void setNumberFreshmen(String numberFreshmen) {
        this.numberFreshmen = numberFreshmen;
    }

    public String getNationalStuAccep() {
        return nationalStuAccep;
    }

    public void setNationalStuAccep(String nationalStuAccep) {
        this.nationalStuAccep = nationalStuAccep;
    }

    public String getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(String tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    public String getNumNationalFreshmen() {
        return numNationalFreshmen;
    }

    public void setNumNationalFreshmen(String numNationalFreshmen) {
        this.numNationalFreshmen = numNationalFreshmen;
    }

    public String getChStuHighRank() {
        return chStuHighRank;
    }

    public void setChStuHighRank(String chStuHighRank) {
        this.chStuHighRank = chStuHighRank;
    }

    public String getChStuWeightRank() {
        return chStuWeightRank;
    }

    public void setChStuWeightRank(String chStuWeightRank) {
        this.chStuWeightRank = chStuWeightRank;
    }

    public String getChGpaAvgStu() {
        return chGpaAvgStu;
    }

    public void setChGpaAvgStu(String chGpaAvgStu) {
        this.chGpaAvgStu = chGpaAvgStu;
    }

    public String getChGpaWeightStu() {
        return chGpaWeightStu;
    }

    public void setChGpaWeightStu(String chGpaWeightStu) {
        this.chGpaWeightStu = chGpaWeightStu;
    }

    public String getChSatAvgLowStu() {
        return chSatAvgLowStu;
    }

    public void setChSatAvgLowStu(String chSatAvgLowStu) {
        this.chSatAvgLowStu = chSatAvgLowStu;
    }

    public String getChSatAvgHighStu() {
        return chSatAvgHighStu;
    }

    public void setChSatAvgHighStu(String chSatAvgHighStu) {
        this.chSatAvgHighStu = chSatAvgHighStu;
    }

    public String getNoSat() {
        return noSat;
    }

    public void setNoSat(String noSat) {
        this.noSat = noSat;
    }

    public int getSatWeight() {
        return satWeight;
    }

    public void setSatWeight(int satWeight) {
        this.satWeight = satWeight;
    }

    public int getActAvgResults() {
        return actAvgResults;
    }

    public void setActAvgResults(int actAvgResults) {
        this.actAvgResults = actAvgResults;
    }

    public int getActWeight() {
        return actWeight;
    }

    public void setActWeight(int actWeight) {
        this.actWeight = actWeight;
    }

    public int getApWeight() {
        return apWeight;
    }

    public void setApWeight(int apWeight) {
        this.apWeight = apWeight;
    }

    public int getIbAvgResults() {
        return ibAvgResults;
    }

    public void setIbAvgResults(int ibAvgResults) {
        this.ibAvgResults = ibAvgResults;
    }

    public int getIbWeight() {
        return ibWeight;
    }

    public void setIbWeight(int ibWeight) {
        this.ibWeight = ibWeight;
    }

    public int getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(int toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }

    public String getExamAp() {
        return examAp;
    }

    public void setExamAp(String examAp) {
        this.examAp = examAp;
    }

    public double getIeltsLowReq() {
        return ieltsLowReq;
    }

    public void setIeltsLowReq(double ieltsLowReq) {
        this.ieltsLowReq = ieltsLowReq;
    }

    public int getTopWeight() {
        return topWeight;
    }

    public void setTopWeight(int topWeight) {
        this.topWeight = topWeight;
    }

    public int getActivitiesWeight() {
        return activitiesWeight;
    }

    public void setActivitiesWeight(int activitiesWeight) {
        this.activitiesWeight = activitiesWeight;
    }

    public int getCompetitionWeight() {
        return competitionWeight;
    }

    public void setCompetitionWeight(int competitionWeight) {
        this.competitionWeight = competitionWeight;
    }

    public int getWelfareWeight() {
        return welfareWeight;
    }

    public void setWelfareWeight(int welfareWeight) {
        this.welfareWeight = welfareWeight;
    }
}
