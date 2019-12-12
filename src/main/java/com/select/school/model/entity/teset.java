package com.select.school.model.entity;

public class teset {
    private String datasix;
    private String databirthday;
    private String datahigh;
    private String dataweight;
    private String dataphone;

    public teset(){
        super();
    }

    public String getDatasix() {
        return datasix;
    }

    public void setDatasix(String datasix) {
        this.datasix = datasix;
    }

    public String getDatabirthday() {
        return databirthday;
    }

    public void setDatabirthday(String databirthday) {
        this.databirthday = databirthday;
    }

    public String getDatahigh() {
        return datahigh;
    }

    public void setDatahigh(String datahigh) {
        this.datahigh = datahigh;
    }

    public String getDataweight() {
        return dataweight;
    }

    public void setDataweight(String dataweight) {
        this.dataweight = dataweight;
    }

    public String getDataphone() {
        return dataphone;
    }

    public void setDataphone(String dataphone) {
        this.dataphone = dataphone;
    }

    public teset(String datasix, String databirthday, String datahigh, String dataweight, String dataphone) {
        this.datasix = datasix;
        this.databirthday = databirthday;
        this.datahigh = datahigh;
        this.dataweight = dataweight;
        this.dataphone = dataphone;
    }
}
