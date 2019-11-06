package com.select.school.controller.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Option;
import com.select.school.service.web.WebProblemService;
import com.select.school.service.wxApplet.ProblemService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.result.ResponseUtil;
import com.select.school.utils.result.AjaxResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "problem")
public class ProblemController {

    @Autowired
    private WebProblemService webProblemService;

    /**
     * 问题接口
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/problemList")
    public String problemList(@RequestBody PagedataDto pagedata,String title) {
        String ajaxResult = webProblemService.selectAll(pagedata,title);
        return ajaxResult;
    }

    /**
     * 根据问题id 查询选项
     * @param
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET}, value = "/selectOption")
    public String findByOption(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        List<Option> options = webProblemService.findByOption(id);
       if (options == null && options.size()<=0){
           return ResponseUtil.setResult(ResponseCode.QUERY_NO_DATAS);
       }
       return ResponseUtil.setResult(ResponseCode.SUCCESS,options);
    }

}
