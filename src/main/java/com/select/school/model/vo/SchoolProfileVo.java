package com.select.school.model.vo;

import com.select.school.model.dto.DreamSchoolDTO;
import com.select.school.model.dto.SafetySchoolDTO;
import com.select.school.model.dto.TargetSchoolDTO;

import java.util.List;

public class SchoolProfileVo {
    private int id;
    private List<DreamSchoolDTO> dreamSchoolDTOS;//梦想学校
    private List<TargetSchoolDTO> targetSchoolDTOS;//目标
    private List<SafetySchoolDTO> safetySchoolDTOS;//保底学校

    public SchoolProfileVo(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DreamSchoolDTO> getDreamSchoolDTOS() {
        return dreamSchoolDTOS;
    }

    public void setDreamSchoolDTOS(List<DreamSchoolDTO> dreamSchoolDTOS) {
        this.dreamSchoolDTOS = dreamSchoolDTOS;
    }

    public List<TargetSchoolDTO> getTargetSchoolDTOS() {
        return targetSchoolDTOS;
    }

    public void setTargetSchoolDTOS(List<TargetSchoolDTO> targetSchoolDTOS) {
        this.targetSchoolDTOS = targetSchoolDTOS;
    }

    public List<SafetySchoolDTO> getSafetySchoolDTOS() {
        return safetySchoolDTOS;
    }

    public void setSafetySchoolDTOS(List<SafetySchoolDTO> safetySchoolDTOS) {
        this.safetySchoolDTOS = safetySchoolDTOS;
    }

}
