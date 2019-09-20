package com.select.school.model.entity;

public class Weight {
    private String id;
    /**
     * 学校简介 id
     */
    private String schoolId;
    /**
     * 学校数据id
     */
    private String scoresId;
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

    public String getScoresId() {
        return scoresId;
    }

    public void setScoresId(String scoresId) {
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
