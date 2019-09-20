package com.select.school.service.impl;

import com.select.school.bean.AjaxResult;
import com.select.school.bean.Const;
import com.select.school.dao.mapper.ProblemMapper;
import com.select.school.dao.mapper.UserScoresMapper;
import com.select.school.model.dto.ProblemDTO;
import com.select.school.model.entity.UserScores;
import com.select.school.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.select.school.bean.AjaxResult.AJAX_DATA;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProblemImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private UserScoresMapper userScoresMapper;

    /**
     * 查询所有问题以及选项
     *
     * @return
     */
    @Override
    public AjaxResult selectProblem() {
        AjaxResult ajaxResult = new AjaxResult();
        List<ProblemDTO> problemList = null;
        try {
            problemList = problemMapper.selectProblems();
        } catch (Exception e) {
            return AjaxResult.error(Const.CodeEnum.badSQL.getCode(), Const.CodeEnum.badSQL.getValue());
        }
        AjaxResult problemListAjaxResult = isNull(problemList);
        ajaxResult.put("problem", problemListAjaxResult);
        return ajaxResult;
    }

    /**
     * 批量插入用户选择的数据
     *
     * @param userScores
     * @return
     */
    @Override
    @Transactional
    public int insertList(UserScores userScores) {
       String id = UUID.randomUUID().toString();
        userScores.setCreateTime(new Date());
        userScores.setId(id);
         userScoresMapper.insertList(userScores);
        return 1;
    }

    private AjaxResult isNull(List list) {
        if (list.size() == 0) {
            AjaxResult ajaxResult = AjaxResult.success(Const.CodeEnum.noObject.getCode(), Const.CodeEnum.noObject.getValue());
            return ajaxResult;
        }
        AjaxResult ajaxResult = AjaxResult.success(Const.CodeEnum.success.getCode(), Const.CodeEnum.success.getValue());
        ajaxResult.put(AJAX_DATA, list);
        return ajaxResult;

    }
}
