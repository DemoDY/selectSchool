package com.select.school.controller.wxApplet;

import com.select.school.utils.result.AjaxResult;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.vo.OptionVo;
import com.select.school.service.wxApplet.ProblemService;
import com.select.school.service.wxApplet.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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
     *
     * @param optionVo
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/optionReceive")
    @ResponseBody
    public AjaxResult optionReceive(@RequestBody OptionVo optionVo) {
        AjaxResult ajaxResult = null;
        try {
            List<OptionDTO> options = optionVo.getOptionList();
            String openid = optionVo.getOpenid();
            String version = optionVo.getVersion();
            // 执行批量保存
            ajaxResult = userScoreService.insertOption(options, openid,version);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(900, "操作失败：系统错误");
        }
        return ajaxResult;
    }


    /**
     * 学校查询
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/schoolList")
    @ResponseBody
    public AjaxResult schoolList(int id) {
        AjaxResult ajaxResult = null;
        try {
            ajaxResult = userScoreService.selectSchool(id);
        }catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(900, "操作失败：系统错误");
        }
        return ajaxResult;
    }
}
