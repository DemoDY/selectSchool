package com.select.school.model.vo;

public class WeightVo {
    private int id;
    /**
     * 学校简介 id
     */
    private int schoolId;
    /**
     * 学校数据id
     */
    private int scoresId;
    /**
     * 权重 (AP, Student take SAT )
     */
    private double apSatWeight;
    /**
     * 权重 (AP, Student take ACT )
     */
    private double apActWeight;
    /**
     * 权重( IB, Student take SAT )
     */
    private double ibSatWeight;
    /**
     * 权重( IB, Student take ACT )
     */
    private double ibActWeight;

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

    public int getScoresId() {
        return scoresId;
    }

    public void setScoresId(int scoresId) {
        this.scoresId = scoresId;
    }

    public double getApSatWeight() {
        return apSatWeight;
    }

    public void setApSatWeight(double apSatWeight) {
        this.apSatWeight = apSatWeight;
    }

    public double getApActWeight() {
        return apActWeight;
    }

    public void setApActWeight(double apActWeight) {
        this.apActWeight = apActWeight;
    }

    public double getIbSatWeight() {
        return ibSatWeight;
    }

    public void setIbSatWeight(double ibSatWeight) {
        this.ibSatWeight = ibSatWeight;
    }

    public double getIbActWeight() {
        return ibActWeight;
    }

    public void setIbActWeight(double ibActWeight) {
        this.ibActWeight = ibActWeight;
    }
}
