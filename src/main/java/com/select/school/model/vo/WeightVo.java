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
    private String apSatWeight;
    /**
     * 权重 (AP, Student take ACT )
     */
    private String apActWeight;
    /**
     * 权重( IB, Student take SAT )
     */
    private String ibSatWeight;
    /**
     * 权重( IB, Student take ACT )
     */
    private String ibActWeight;

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

    public String getApSatWeight() {
        return apSatWeight;
    }

    public void setApSatWeight(String apSatWeight) {
        this.apSatWeight = apSatWeight;
    }

    public String getApActWeight() {
        return apActWeight;
    }

    public void setApActWeight(String apActWeight) {
        this.apActWeight = apActWeight;
    }

    public String getIbSatWeight() {
        return ibSatWeight;
    }

    public void setIbSatWeight(String ibSatWeight) {
        this.ibSatWeight = ibSatWeight;
    }

    public String getIbActWeight() {
        return ibActWeight;
    }

    public void setIbActWeight(String ibActWeight) {
        this.ibActWeight = ibActWeight;
    }
}
