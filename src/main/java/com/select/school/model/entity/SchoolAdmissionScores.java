package com.select.school.model.entity;

public class SchoolAdmissionScores {
    private String id;
    /**
     * 学校简介 id
     */
    private String schoolId;
    private int firstStudentsNum;//留学生大一入学人数
    private String comAdmissionRate;//2019 综合录取率
    private int numberFreshmen;//大一新生人数
    private String nationalStuAccep;//国际生录取率
    private double tuitionFees;//学费（美元）
    private int numNationalFreshmen;//大一国际生人数
    private String chStuHighRank;//中国录取学生高中年级排名
    private int chStuWeightRank;//中国学生排名权重
    private double chGpaAvgStu;//中国学生平均GPA
    private String chGpaWeightStu;//中国学生GPA权重
    private double chSatAvgLowStu;//中国学生SAT平均低位成绩
    private double chSatAvgHighStu;//中国学生SAT平均高位成绩
    private double satWeight;//SAT权重
    private double actAvgResults;//ACT平均成绩
    private double actWeight;//ACT权重
    private String apNumCourse;//AP 选课数量
    private int apWeight;//AP 选课权重
    private double ibAvgResults;//IB 平均成绩
    private int ibWeight;//IB 权重
    private double toeflLowReq;//TOEFL 最低要求
    private double ieltsLowReq;//IELTS 最低要求
    private double apSat;//学生选择AP，或者不选，提供SAT成绩
    private double apAct;//学生选择AP，或者不选，提供ACT成绩
    private double ibSat;//学生选择IB，提供SAT成绩
    private double ibAct;// 学生选择IB，提供ACT成绩

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
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

    public int getNumberFreshmen() {
        return numberFreshmen;
    }

    public void setNumberFreshmen(int numberFreshmen) {
        this.numberFreshmen = numberFreshmen;
    }

    public String getNationalStuAccep() {
        return nationalStuAccep;
    }

    public void setNationalStuAccep(String nationalStuAccep) {
        this.nationalStuAccep = nationalStuAccep;
    }

    public double getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(double tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    public int getNumNationalFreshmen() {
        return numNationalFreshmen;
    }

    public void setNumNationalFreshmen(int numNationalFreshmen) {
        this.numNationalFreshmen = numNationalFreshmen;
    }

    public String getChStuHighRank() {
        return chStuHighRank;
    }

    public void setChStuHighRank(String chStuHighRank) {
        this.chStuHighRank = chStuHighRank;
    }

    public int getChStuWeightRank() {
        return chStuWeightRank;
    }

    public void setChStuWeightRank(int chStuWeightRank) {
        this.chStuWeightRank = chStuWeightRank;
    }

    public double getChGpaAvgStu() {
        return chGpaAvgStu;
    }

    public void setChGpaAvgStu(double chGpaAvgStu) {
        this.chGpaAvgStu = chGpaAvgStu;
    }

    public String getChGpaWeightStu() {
        return chGpaWeightStu;
    }

    public void setChGpaWeightStu(String chGpaWeightStu) {
        this.chGpaWeightStu = chGpaWeightStu;
    }

    public double getChSatAvgLowStu() {
        return chSatAvgLowStu;
    }

    public void setChSatAvgLowStu(double chSatAvgLowStu) {
        this.chSatAvgLowStu = chSatAvgLowStu;
    }

    public double getChSatAvgHighStu() {
        return chSatAvgHighStu;
    }

    public void setChSatAvgHighStu(double chSatAvgHighStu) {
        this.chSatAvgHighStu = chSatAvgHighStu;
    }

    public double getSatWeight() {
        return satWeight;
    }

    public void setSatWeight(double satWeight) {
        this.satWeight = satWeight;
    }

    public double getActAvgResults() {
        return actAvgResults;
    }

    public void setActAvgResults(double actAvgResults) {
        this.actAvgResults = actAvgResults;
    }

    public double getActWeight() {
        return actWeight;
    }

    public void setActWeight(double actWeight) {
        this.actWeight = actWeight;
    }

    public String getApNumCourse() {
        return apNumCourse;
    }

    public void setApNumCourse(String apNumCourse) {
        this.apNumCourse = apNumCourse;
    }

    public int getApWeight() {
        return apWeight;
    }

    public void setApWeight(int apWeight) {
        this.apWeight = apWeight;
    }

    public double getIbAvgResults() {
        return ibAvgResults;
    }

    public void setIbAvgResults(double ibAvgResults) {
        this.ibAvgResults = ibAvgResults;
    }

    public int getIbWeight() {
        return ibWeight;
    }

    public void setIbWeight(int ibWeight) {
        this.ibWeight = ibWeight;
    }

    public double getToeflLowReq() {
        return toeflLowReq;
    }

    public void setToeflLowReq(double toeflLowReq) {
        this.toeflLowReq = toeflLowReq;
    }

    public double getIeltsLowReq() {
        return ieltsLowReq;
    }

    public void setIeltsLowReq(double ieltsLowReq) {
        this.ieltsLowReq = ieltsLowReq;
    }

    public double getApSat() {
        return apSat;
    }

    public void setApSat(double apSat) {
        this.apSat = apSat;
    }

    public double getApAct() {
        return apAct;
    }

    public void setApAct(double apAct) {
        this.apAct = apAct;
    }

    public double getIbSat() {
        return ibSat;
    }

    public void setIbSat(double ibSat) {
        this.ibSat = ibSat;
    }

    public double getIbAct() {
        return ibAct;
    }

    public void setIbAct(double ibAct) {
        this.ibAct = ibAct;
    }
}
