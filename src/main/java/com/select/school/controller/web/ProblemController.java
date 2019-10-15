package com.select.school.controller.web;

import com.select.school.service.wxApplet.ProblemService;
import com.select.school.utils.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
    public AjaxResult problemList() {
        AjaxResult ajaxResult = problemService.selectProblems();
        return ajaxResult;
    }


}
