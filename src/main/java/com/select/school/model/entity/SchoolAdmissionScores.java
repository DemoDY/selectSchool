package com.select.school.model.entity;

public class SchoolAdmissionScores {
    private int id;
    /**
     * 学校简介 id230
     */
    private int schoolId;
    private int nineteen;//2019 录取人数
    private int twenty;//2020 录取人数/
    private String firstStudentsNum;//留学生大一入学人数
    private String comAdmissionRate;//2019 综合录取率
    private String numberFreshmen;//大一新生人数
    private String nationalStuAccep;//国际生录取率
    private double tuitionFees;//学费（美元）
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
    private int toeflLowReq;//TOEFL 最低要求
    private double ieltsLowReq;//IELTS 最低要求
    private int apSat;//学生选择AP，或者不选，提供SAT成绩
    private int apAct;//学生选择AP，或者不选，提供ACT成绩
    private int ibSat;//学生选择IB，提供SAT成绩
    private int ibAct;// 学生选择IB，提供ACT成绩
    private String addValue;//增值活动

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(double tuitionFees) {
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

    public int getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(int toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }

    public double getIeltsLowReq() {
        return ieltsLowReq;
    }

    public void setIeltsLowReq(double ieltsLowReq) {
        this.ieltsLowReq = ieltsLowReq;
    }

    public int getApSat() {
        return apSat;
    }

    public void setApSat(int apSat) {
        this.apSat = apSat;
    }

    public int getApAct() {
        return apAct;
    }

    public void setApAct(int apAct) {
        this.apAct = apAct;
    }

    public int getIbSat() {
        return ibSat;
    }

    public void setIbSat(int ibSat) {
        this.ibSat = ibSat;
    }

    public int getIbAct() {
        return ibAct;
    }

    public void setIbAct(int ibAct) {
        this.ibAct = ibAct;
    }

    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }
}
