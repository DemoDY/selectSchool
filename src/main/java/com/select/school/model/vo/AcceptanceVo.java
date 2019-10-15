package com.select.school.model.vo;

import com.select.school.model.dto.DreamAcceptanceDTO;
import com.select.school.model.dto.SafetyAcceptanceDTO;
import com.select.school.model.dto.TargetAcceptanceDTO;

import java.util.List;

/**
 * 录取率
 */
public class AcceptanceVo {
    private List<DreamAcceptanceDTO> dream;
    private List<TargetAcceptanceDTO> target;
    private List<SafetyAcceptanceDTO> safety;

    public List<DreamAcceptanceDTO> getDream() {
        return dream;
    }

    public void setDream(List<DreamAcceptanceDTO> dream) {
        this.dream = dream;
    }

    public List<TargetAcceptanceDTO> getTarget() {
        return target;
    }

    public void setTarget(List<TargetAcceptanceDTO> target) {
        this.target = target;
    }

    public List<SafetyAcceptanceDTO> getSafety() {
        return safety;
    }

    public void setSafety(List<SafetyAcceptanceDTO> safety) {
        this.safety = safety;
    }
}
