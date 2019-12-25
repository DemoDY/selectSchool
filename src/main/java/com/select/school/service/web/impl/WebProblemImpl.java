package com.select.school.service.web.impl;

import com.select.school.mapper.OptionMapper;
import com.select.school.mapper.ProblemMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Option;
import com.select.school.model.entity.Problem;
import com.select.school.service.web.WebProblemService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebProblemImpl implements WebProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private OptionMapper optionMapper;
    /**
     * 查询问题列表
     * @param pagedata
     * @return
     */
    @Override
    public String   selectAll(PagedataDto pagedata){
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();
        List<Problem> problemList = problemMapper.selectAll(SqlParameter.getParameter().addLimit(pagedata.getPageNum(),
                pagedata.getPageSize()).addQuery("title",pagedata.getTitle()).getMap());
        int count = problemMapper.count(SqlParameter.getParameter().addQuery("title",pagedata.getTitle()).getMap());
        if (problemList == null || problemList.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<Problem> list = new ArrayList<>();
            list.addAll(problemList);
            pagedataDto.setRecords(problemList);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()+1));
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
    }

    @Override
    public List<Option> findByOption(int id){
        return  optionMapper.selectProblemId(id);
    }
}
