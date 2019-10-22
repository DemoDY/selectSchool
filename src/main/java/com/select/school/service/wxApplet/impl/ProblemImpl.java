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

    @Override
    public AjaxResult selectProblems(){
        AjaxResult ajaxResult = new AjaxResult();
        List<ProblemDTO> problemList = problemMapper.selectProblems();
        AjaxResult problemListAjaxResult = isNull(problemList);
        ajaxResult.put("problem", problemListAjaxResult);
        return ajaxResult;
    }

    /**
     * 查询问题列表
     * @param pagedata
     * @return
     */
    @Override
    public String selectAll(PagedataDto pagedata){
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();

        List<Problem> problemList = problemMapper.selectAll(SqlParameter.getParameter().addLimit(pagedata.getPageNum(), pagedata.getPageSize()).getMap());
        int count = problemMapper.count(SqlParameter.getParameter().getMap());
        if (problemList == null || problemList.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<Problem> list = new ArrayList<>();
            list.addAll(problemList);
            pagedataDto.setRecords(problemList);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()));
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
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
