package com.select.school.service.wxApplet.impl;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Problem;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.result.AjaxResult;
import com.select.school.utils.result.Const;
import com.select.school.mapper.ProblemMapper;
import com.select.school.model.dto.ProblemDTO;
import com.select.school.service.wxApplet.ProblemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import static com.select.school.utils.result.AjaxResult.AJAX_DATA;

@Service
public class ProblemImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    /**
     * 查询小程序所需要的问题列表
     * @return
     */
    @Override
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
