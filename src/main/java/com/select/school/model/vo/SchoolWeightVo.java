package com.select.school.model.vo;

//最后权重  学校权重
public class SchoolWeightVo {
    private int apSat;//学生选择AP，或者不选，提供SAT成绩
    private int apAct;//学生选择AP，或者不选，提供ACT成绩
    private int ibSat;//学生选择IB，提供SAT成绩
    private int ibAct;// 学生选择IB，提供ACT成绩

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
}
