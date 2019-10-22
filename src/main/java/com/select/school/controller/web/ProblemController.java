package com.select.school.controller.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.service.wxApplet.ProblemService;
import com.select.school.utils.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(value = "problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    /**
     * 问题接口
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/problemList")
    @ResponseBody
    public String problemList(@RequestBody PagedataDto pagedata) {
        String ajaxResult = problemService.selectAll(pagedata);
        return ajaxResult;
    }

}
