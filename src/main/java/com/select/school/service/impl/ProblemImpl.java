package com.select.school.service.impl;

import com.select.school.bean.AjaxResult;
import com.select.school.bean.Const;
import com.select.school.dao.mapper.ProblemMapper;
import com.select.school.model.dto.ProblemDTO;
import com.select.school.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.select.school.bean.AjaxResult.AJAX_DATA;

@Service
public class ProblemImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    public AjaxResult selectProblems(){
        AjaxResult ajaxResult = new AjaxResult();
        List<ProblemDTO> problemList = problemMapper.selectProblems();
        AjaxResult problemListAjaxResult = isNull(problemList);
        ajaxResult.put("problem", problemListAjaxResult);
        return ajaxResult;
    }


    private AjaxResult isNull(List list) {
        if (list.size() == 0) {
            AjaxResult ajaxResult = AjaxResult.success(Const.CodeEnum.noObject.getCode(), Const.CodeEnum.noObject.getValue());
            return ajaxResult;
        }
        AjaxResult ajaxResult = AjaxResult.success(Const.CodeEnum.success.getCode(), Const.CodeEnum.success.getValue());
        ajaxResult.put(AJAX_DATA, list);
        return ajaxResult;
    }
}
