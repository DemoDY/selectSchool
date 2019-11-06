package com.select.school.service.web.impl;

import com.select.school.mapper.SchoolAdmissionScoresMapper;
import com.select.school.mapper.SchoolProfileMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.SchoolAdmissionScores;
import com.select.school.model.entity.SchoolProfile;
import com.select.school.service.web.WebSchoolService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebSchoolImpl implements WebSchoolService {

    @Autowired
    private SchoolProfileMapper schoolProfileMapper;

    @Autowired
    private SchoolAdmissionScoresMapper schoolAdmissionScoresMapper;

    @Override
    public String selectAll(PagedataDto pagedata,String title){
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();
        List<SchoolProfile> schoolProfiles = schoolProfileMapper.selectAll(SqlParameter.getParameter().addLimit(pagedata.getPageNum(), pagedata.getPageSize()).addQuery("schoolName",title).getMap());
        int count = schoolProfileMapper.count(SqlParameter.getParameter().getMap());
        if (schoolProfiles == null || schoolProfiles.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<SchoolProfile> list = new ArrayList<>();
            list.addAll(schoolProfiles);
            pagedataDto.setRecords(schoolProfiles);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()));
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
    }

    @Override
    public SchoolAdmissionScores selectSchool(int id){
        return schoolAdmissionScoresMapper.selectById(id);
    }
}
