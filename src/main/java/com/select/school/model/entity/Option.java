package com.select.school.model.entity;

public class Option {
    private int id;
    /**
     * 问题id
     */
    private int problemId;
    /**
     * 选项
     */
    private String option;
    /**
     * 分数
     */
    private String number;
    /**
     * 雅思或者托福
     */
    private String tl;
    /**
     * 选项描述
     */
    private String description;

    /**
     * 答案序号
     */
    private int seq;

    public Option() {
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", problemId=" + problemId +
                ", option='" + option + '\'' +
                ", number='" + number + '\'' +
                ", tl='" + tl + '\'' +
                ", description='" + description + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }

    public Option(int id, int problemId, String option, String number, String tl, String description,int seq) {
        this.id = id;
        this.problemId = problemId;
        this.option = option;
        this.number = number;
        this.tl = tl;
        this.description = description;
        this.seq = seq;
    }
}
