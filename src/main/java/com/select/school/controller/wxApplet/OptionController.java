package com.select.school.controller.wxApplet;

import com.select.school.model.vo.SchoolProfileVo;
import com.select.school.utils.result.AjaxResult;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.vo.OptionVo;
import com.select.school.service.wxApplet.ProblemService;
import com.select.school.service.wxApplet.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "option")
public class OptionController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserScoreService userScoreService;

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

    /**
     * 接收小程序传输数据
     * @param optionVo
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/optionReceive")
    @ResponseBody
    public AjaxResult optionReceive(@RequestBody OptionVo optionVo) {
        try {
            List<OptionDTO> options = optionVo.getOptionList();
            // 执行批量保存
            userScoreService.insertOption(options);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(900, "操作失败：系统错误");
        }
        return AjaxResult.success(AjaxResult.CODE_SUCCESS, "操作成功");
    }


    /**
     * 学校查询
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/schoolList")
    @ResponseBody
    public AjaxResult schoolList(String openId) {
        AjaxResult ajaxResult = userScoreService.selectSchool(openId);
        return ajaxResult;
    }
}
