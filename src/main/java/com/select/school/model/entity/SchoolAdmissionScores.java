package com.select.school.model.entity;

public class SchoolAdmissionScores {
    private int id;
    /**
     * 学校简介 id
     */
    private int schoolId;
    private String nineteen;//2019 录取人数
    private String twenty;//2020 录取人数
    private String firstStudentsNum;//留学生大一入学人数
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
    private String satWeight;//SAT权重
    private String actAvgResults;//ACT平均成绩
    private String actWeight;//ACT权重
    private String apNumCourse;//AP 选课数量
    private String apWeight;//AP 选课权重
    private String ibAvgResults;//IB 平均成绩
    private String ibWeight;//IB 权重
    private String toeflLowReq;//TOEFL 最低要求
    private String ieltsLowReq;//IELTS 最低要求
    private String apSat;//学生选择AP，或者不选，提供SAT成绩
    private String apAct;//学生选择AP，或者不选，提供ACT成绩
    private String ibSat;//学生选择IB，提供SAT成绩
    private String ibAct;// 学生选择IB，提供ACT成绩
    private String addValue;//增值活动

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNineteen() {
        return nineteen;
    }

    public void setNineteen(String nineteen) {
        this.nineteen = nineteen;
    }

    public String getTwenty() {
        return twenty;
    }

    public void setTwenty(String twenty) {
        this.twenty = twenty;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getFirstStudentsNum() {
        return firstStudentsNum;
    }

    public void setFirstStudentsNum(String firstStudentsNum) {
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

    public String getSatWeight() {
        return satWeight;
    }

    public void setSatWeight(String satWeight) {
        this.satWeight = satWeight;
    }

    public String getActAvgResults() {
        return actAvgResults;
    }

    public void setActAvgResults(String actAvgResults) {
        this.actAvgResults = actAvgResults;
    }

    public String getActWeight() {
        return actWeight;
    }

    public void setActWeight(String actWeight) {
        this.actWeight = actWeight;
    }

    public String getApNumCourse() {
        return apNumCourse;
    }

    public void setApNumCourse(String apNumCourse) {
        this.apNumCourse = apNumCourse;
    }

    public String getApWeight() {
        return apWeight;
    }

    public void setApWeight(String apWeight) {
        this.apWeight = apWeight;
    }

    public String getIbAvgResults() {
        return ibAvgResults;
    }

    public void setIbAvgResults(String ibAvgResults) {
        this.ibAvgResults = ibAvgResults;
    }

    public String getIbWeight() {
        return ibWeight;
    }

    public void setIbWeight(String ibWeight) {
        this.ibWeight = ibWeight;
    }

    public String getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(String toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }

    public String getIeltsLowReq() {
        return ieltsLowReq;
    }

    public void setIeltsLowReq(String ieltsLowReq) {
        this.ieltsLowReq = ieltsLowReq;
    }

    public String getApSat() {
        return apSat;
    }

    public void setApSat(String apSat) {
        this.apSat = apSat;
    }

    public String getApAct() {
        return apAct;
    }

    public void setApAct(String apAct) {
        this.apAct = apAct;
    }

    public String getIbSat() {
        return ibSat;
    }

    public void setIbSat(String ibSat) {
        this.ibSat = ibSat;
    }

    public String getIbAct() {
        return ibAct;
    }

    public void setIbAct(String ibAct) {
        this.ibAct = ibAct;
    }

    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }
}
