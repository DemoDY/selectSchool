package com.select.school.model.dto;

public class SchoolDetails {
    private String name;
    private String details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "SchoolDetails{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
