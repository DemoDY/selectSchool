package com.select.school.control;

import com.select.school.bean.AjaxResult;
import com.select.school.model.entity.UserScores;
import com.select.school.service.ProblemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/problem")
@RestController
public class ProblemController {
    private static Logger logger = LoggerFactory.getLogger(ProblemController.class);
    @Autowired
    private ProblemService problemService;

    /**
     * 问题接口
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/problem-list")
    public AjaxResult problemList() {
        AjaxResult ajaxResult = problemService.selectProblem();
        return ajaxResult;
    }

    /**
     * app问题  接收用户选择得分
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/problem-receive")
    @ResponseBody
    public AjaxResult problemReceive(@RequestBody UserScores userScores) {
        logger.info("打印记录：{用户名：" + userScores.getUsername()+"，openId:"+userScores.getOpenId()+
                "，分数："+userScores.getScores());
        try {
            // 执行批量保存
            problemService.insertList(userScores);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(900, "操作失败：系统错误");
        }
        return AjaxResult.success(AjaxResult.CODE_SUCCESS, "操作成功");
    }
}
