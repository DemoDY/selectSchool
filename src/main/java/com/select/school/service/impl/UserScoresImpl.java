package com.select.school.service.impl;

import com.select.school.bean.AjaxResult;
import com.select.school.bean.BeanCopierEx;
import com.select.school.dao.mapper.SchoolProfileMapper;
import com.select.school.dao.mapper.UserScoresMapper;
import com.select.school.dao.mapper.WeightMapper;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.SchoolProfile;
import com.select.school.model.entity.UserScores;
import com.select.school.model.entity.Weight;
import com.select.school.model.vo.SchoolProfileVo;
import com.select.school.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserScoresImpl implements UserScoreService {

    @Autowired
    private UserScoresMapper userScoresMapper;
    @Autowired
    private WeightMapper weightMapper;
    @Autowired
    private SchoolProfileMapper schoolProfileMapper;

    /**
     * 微信学生选择之后返回数据
     *
     * @param options
     * @return
     */
    public AjaxResult selectOption(List<OptionDTO> options) {
        int num = 0;
        String ielts = "";
        String toefl = "";
        String act_sat = "";
        String ib_ap = "";
        String username = "230242591";
        String sex = "";
        UserScores userScores = new UserScores();
        String id = UUID.randomUUID().toString();
        for (OptionDTO option : options) {
            int problemId = option.getProblemId();
            //判断问题分数不等于 并且分数不等于0 并且第一题不算入分数中
            String number = option.getNumber();
            if (number != null && !number.equals("0")) {
                if (problemId == 12) {
                    act_sat = "SAT";
                } else if (problemId == 13) {
                    act_sat = "ACT";
                }
                //问题
                if (problemId == 15) {
                    ib_ap = "AP";
                } else if (problemId == 16) {
                    ib_ap = "IB";
                }
                int i = Integer.parseInt(number);
                num += i; //总分数
            }
            //获取性别
            if (problemId == 1) {
                sex = option.getOption();
            }

            String tl = option.getTl();
            //选择雅思或者托福成绩
            if (tl != null && tl != "") {
                //雅思 IELTS
                if (tl.equals("IELTS")) {
                    ielts = option.getOption();
                }
                //托福 TOEFL
                if (tl.equals("TOEFL")) {
                    toefl = option.getOption();
                }
            }
        }
        if (toefl != "" && ielts != "") {
            if (toefl.equals(">=100")) {
                // 1 如果 托福 是大于100分 以托福为主。不管雅思多少分c
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }
            if (toefl.equals("99—90")) {
                // 2 如果 托福 是99—90分 而 雅思是 7.5以上  以雅思为主 否则以托福为主。
                if (ielts.equals("7.5")) {
                    userScores.setTl(ielts);
                }
                if (ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }

            if (toefl.equals("89—79")) {
                // 3 如果 托福 是89—79分 而雅思是 7以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7")) {
                    userScores.setTl(ielts);
                }
                if (ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }
            if (toefl.equals("78—69")) {
                // 4 如果 托福 是78—69分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5")) {
                    userScores.setTl(ielts);
                }
                if (ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }
            if (toefl.equals("68-61")) {
                // 5 如果 托福 是68-61分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6")) {
                    userScores.setTl(ielts);
                }
                if (ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }
            if (toefl.equals("小于61")) {
                if (ielts.equals("低于5.5")) {
                    userScores.setTl(toefl);
                }
            }
        }
        if (toefl != "" && ielts == "") {
            userScores.setTl(toefl);
        }
        if (toefl == "" && ielts != "") {
            userScores.setTl(ielts);
        }
        if (sex.equals("女")) {
            //因为本来分数是已经加到一起的 所以 如果小于500就减去原有的10分
            //如果是女生 ap 成绩大于等于500 加10分
            if (ib_ap.equals("AP") && num < 500) {
                num = num - 10;
            }
            //ib成绩大于等于540 加十分
            if (ib_ap.equals("Ib") && num < 540) {
                num = num - 10;
            }
        }
        System.out.println(num);

        userScores.setCreateTime(new Date());
        userScores.setScores(num);
        userScores.setActSat(act_sat);
        userScores.setApIb(ib_ap);
        userScores.setUsername(username);
        userScores.setOpenId("92302121");
        userScores.setId(id);

//        userScoresMapper.insertList(userScores);
//        selectSchool(username);
        return AjaxResult.success();
    }

    /**
     * 根据学生成绩查询 九所学校
     *
     * @param username
     * @return
     */
    public AjaxResult selectSchool(String username) {
        AjaxResult ajaxResult = new AjaxResult();
        UserScores userScores = userScoresMapper.selectUsername(username);
        String sat_act = userScores.getActSat();
        String ib_ap = userScores.getApIb();
        String tl = userScores.getTl();
        List<SchoolProfileVo> schoolProfileVos = new ArrayList<>();
        List<Weight> weights = weightMapper.selectWeightList();
        List<Weight> weightSelect = null;
        SchoolProfile schoolProfile = null;
        for (Weight weight : weights) {
            SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
            if (sat_act.equals("ACT")) {
                if (ib_ap.equals("IB")) {//act ib
                    weightSelect = weightMapper.selectIbActWeightDream(userScores.getScores() + 50);
//                    schoolProfile = schoolProfileMapper.selectById(weightSelect.getSchoolId());
                    BeanCopierEx.copy(schoolProfile, schoolProfileVo);
                    schoolProfileVo.setDreamSchool("dream");
                    schoolProfileVos.add(schoolProfileVo);
                    System.out.println("IB ACT_IB===" + userScores.getScores());
                }
                if (ib_ap.equals("AP")) {//act ap
                    double act_ap = weight.getApActWeight();
                    System.out.println("AP ACT_AP===" + userScores.getScores());
                }
            }
            if (sat_act.equals("SAT")) {
                if (ib_ap.equals("IB")) {//sat ib
                    double sat_ib = weight.getIbSatWeight();
                    System.out.println("IB SAT_IB===" + userScores.getScores());
                }
                if (ib_ap.equals("AP")) {//sat ap
                    weightSelect = weightMapper.selectIbActWeightDream(userScores.getScores() + 50);
//                    schoolProfile = schoolProfileMapper.selectById(weightSelect.getSchoolId());
//                    BeanCopierEx.copy(schoolProfile,schoolProfileVo);
//                    schoolProfileVo.setDreamSchool("dream");
//                    schoolProfileVos.add(schoolProfileVo);
                    System.out.println("AP SAT_AP===" + userScores.getScores());
                }
            }
        }
        System.out.println("userScore==" + userScores.getActSat());
        ajaxResult.put("school", schoolProfileVos);
        return ajaxResult;
    }
}
