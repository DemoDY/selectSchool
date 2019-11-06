package com.select.school.model.dto;

public class OptionDTO {
    private  String id;
    private String problemId;
    private String option;
    private String number;
    private String tl;//托福或雅思
    private String skipDown;
    private String description;
    private int seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkipDown() {
        return skipDown;
    }

    public void setSkipDown(String skipDown) {
        this.skipDown = skipDown;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "OptionDTO{" +
                "id=" + id +
                ", problemId=" + problemId +
                ", option='" + option + '\'' +
                ", number='" + number + '\'' +
                ", tl='" + tl + '\'' +
                ", description='" + description + '\'' +
                ", skipDown='" + skipDown + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }
}
