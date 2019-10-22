package com.select.school.service.wxApplet;


import com.select.school.model.dto.PagedataDto;
import com.select.school.utils.result.AjaxResult;

public interface ProblemService {
    String selectAll(PagedataDto pagedata);
    AjaxResult selectProblems();
}
