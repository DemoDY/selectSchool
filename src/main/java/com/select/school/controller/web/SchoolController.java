package com.select.school.controller.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.dto.SchoolDTO;
import com.select.school.model.entity.SchoolAdmissionScores;
import com.select.school.service.web.WebSchoolService;
import com.select.school.utils.FileUploadUtils;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseUtil;
import com.select.school.utils.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.select.school.utils.result.AjaxResult.error;
import static com.select.school.utils.result.AjaxResult.success;

@RestController
@CrossOrigin
@RequestMapping(value = "school")
public class SchoolController {

    @Autowired
    private WebSchoolService webSchoolService;

    /**
     * 查询学校详情列表
     *
     * @param pagedata
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/schoolList")
    public String schoolList(@RequestBody PagedataDto pagedata) {
        String schoolList = webSchoolService.selectAll(pagedata);
        return schoolList;
    }


    /**
     * 根据学校id 查询学校数据
     *
     * @param
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET}, value = "/selectSchoolDate")
    public String findByOption(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SchoolAdmissionScores schoolProfiles = webSchoolService.selectSchool(id);
        if (schoolProfiles == null) {
            return ResponseUtil.setResult(ResponseCode.QUERY_NO_DATAS);
        }
        return ResponseUtil.setResult(ResponseCode.SUCCESS, schoolProfiles);
    }

    /**
     * 学校详情 数据新增
     *
     * @param schoolDTO
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/addSchool")
    public AjaxResult addSchool(@RequestBody SchoolDTO schoolDTO) {
        int i = webSchoolService.addSchool(schoolDTO);
        if (i > 0) {
            return success();
        } else {
            return error();
        }
    }

    /**
     * 获取学校所有信息
     *
     * @param request
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/editSchool")
    public String editSchoolDate(HttpServletRequest request) {
        String id = request.getParameter("schoolId");
        int schoolId = Integer.parseInt(id);
        SchoolDTO schoolDTO = webSchoolService.findById(schoolId);
        return ResponseUtil.setResult(ResponseCode.SUCCESS, schoolDTO);
    }

    /**
     * 修改学校数据信息
     *
     * @param schoolDTO
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/updateSchool")
    public AjaxResult updateSchool(@RequestBody SchoolDTO schoolDTO) {
        int i = webSchoolService.updateSchool(schoolDTO);
        if (i<=0){
            return error();
        }else {
            return success();
        }
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                //文件上传获取文件名字
                String files = FileUploadUtils.upload(file);
                System.out.println("===fileName==="+files);
                if (files != null && files!="") {
                    return AjaxResult.successData(AjaxResult.CODE_SUCCESS, files);
                }
            }
            return error();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}
