package com.select.school.model.entity;

public class Weight {
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
    private int apSatWeight;
    /**
     * 权重 (AP, Student take ACT )
     */
    private int apActWeight;
    /**
     * 权重( IB, Student take SAT )
     */
    private int ibSatWeight;
    /**
     * 权重( IB, Student take ACT )
     */
    private int ibActWeight;

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

    public int getApSatWeight() {
        return apSatWeight;
    }

    public void setApSatWeight(int apSatWeight) {
        this.apSatWeight = apSatWeight;
    }

    public int getApActWeight() {
        return apActWeight;
    }

    public void setApActWeight(int apActWeight) {
        this.apActWeight = apActWeight;
    }

    public int getIbSatWeight() {
        return ibSatWeight;
    }

    public void setIbSatWeight(int ibSatWeight) {
        this.ibSatWeight = ibSatWeight;
    }

    public int getIbActWeight() {
        return ibActWeight;
    }

    public void setIbActWeight(int ibActWeight) {
        this.ibActWeight = ibActWeight;
    }
}
