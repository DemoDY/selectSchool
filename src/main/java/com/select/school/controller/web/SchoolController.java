package com.select.school.controller.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.SchoolAdmissionScores;
import com.select.school.service.web.WebSchoolService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "school")
public class SchoolController {

    @Autowired
    private WebSchoolService webSchoolService;

    /**
     * 查询学校详情列表
     * @param pagedata
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/schoolList")
    public String schoolList(@RequestBody PagedataDto pagedata,String title) {
        String schoolList = webSchoolService.selectAll(pagedata,title);
        return schoolList;
    }


    /**
     * 根据学校id 查询学校数据
     * @param
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET}, value = "/selectSchoolDate")
    public String findByOption(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SchoolAdmissionScores schoolProfiles = webSchoolService.selectSchool(id);
        if (schoolProfiles == null){
            return ResponseUtil.setResult(ResponseCode.QUERY_NO_DATAS);
        }
        return ResponseUtil.setResult(ResponseCode.SUCCESS,schoolProfiles);
    }
}
