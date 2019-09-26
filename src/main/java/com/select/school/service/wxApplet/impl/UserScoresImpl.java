package com.select.school.service.wxApplet.impl;

import com.select.school.mapper.SchoolAdmissionScoresMapper;
import com.select.school.utils.BeanCopierEx;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.result.AjaxResult;
import com.select.school.mapper.SchoolProfileMapper;
import com.select.school.mapper.UserScoresMapper;
import com.select.school.mapper.WeightMapper;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.SchoolProfile;
import com.select.school.model.entity.UserScores;
import com.select.school.model.entity.Weight;
import com.select.school.model.vo.SchoolProfileVo;
import com.select.school.service.wxApplet.UserScoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserScoresImpl implements UserScoreService {

    @Autowired
    private UserScoresMapper userScoresMapper;
    @Autowired
    private WeightMapper weightMapper;
    @Autowired
    private SchoolProfileMapper schoolProfileMapper;
    @Autowired
    private SchoolAdmissionScoresMapper schoolAdmissionScoresMapper;

    /**
     * 微信学生选择之后返回数据
     *
     * @param options
     * @return
     */
    public AjaxResult insertOption(List<OptionDTO> options) {
        int num = 0;
        String ielts = "";
        String toefl = "";
        String act_sat = "";
        String ib_ap = "";
        String sex = "";
        String openId = "";
        UserScores userScores = new UserScores();
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
            openId = option.getOpenid();
        }
        if (StringUtils.isNotBlank(toefl) && StringUtils.isNotBlank(ielts)) {
            if (toefl.equals(">=100")) {
                // 1 如果 托福 是大于100分 以托福为主。不管雅思多少分c
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl("TOEFL");
                    userScores.setTlScore(toefl);
                }
            }
            if (toefl.equals("99—90")) {
                // 2 如果 托福 是99—90分 而 雅思是 7.5以上  以雅思为主 否则以托福为主。
                if (ielts.equals("7.5")) {
                    userScores.setTl("IELTS");
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl("TOEFL");
                    userScores.setTlScore(toefl);
                }
            }

            if (toefl.equals("89—79")) {
                // 3 如果 托福 是89—79分 而雅思是 7以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7")) {
                    userScores.setTl("IELTS");
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl("TOEFL");
                    userScores.setTlScore(toefl);
                }
            }
            if (toefl.equals("78—69")) {
                // 4 如果 托福 是78—69分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5")) {
                    userScores.setTl("IELTS");
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl("TOEFL");
                }
            }
            if (toefl.equals("68-61")) {
                // 5 如果 托福 是68-61分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6")) {
                    userScores.setTl("IELTS");
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl("TOEFL");
                }
            }
            if (toefl.equals("小于61")) {
                if (ielts.equals("低于5.5")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl("TOEFL");
                }
            }
        }
        //如果 托福不为空 雅思为空 则保存托福成绩
        if (StringUtils.isNotBlank(toefl) && StringUtils.isEmpty(ielts)) {
            userScores.setTlScore(toefl);
            userScores.setTl("TOEFL");
        }
        //如果 雅思不为空 托福为空 则保存雅思成绩
        if (toefl == "" && ielts != "") {
            userScores.setTlScore(ielts);
            userScores.setTl("IELTS");
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
        userScores.setCreateTime(new Date());
        userScores.setScores(num);
        userScores.setActSat(act_sat);
        userScores.setApIb(ib_ap);
        userScores.setOpenId(openId);
        userScoresMapper.insertList(userScores);
//        selectSchool(openId);
        return AjaxResult.success();
    }

    /**
     * 根据学生成绩查询 九所学校
     *
     * @param openId
     * @return
     * @
     */
    public AjaxResult selectSchool(String openId) {
        AjaxResult ajaxResult = new AjaxResult();
        UserScores userScores = userScoresMapper.selectOpenId(openId);
        String sat_act = userScores.getActSat();
        String ib_ap = userScores.getApIb();
        String tl = userScores.getTl();
        String tlScore = userScores.getTlScore();
        List<SchoolProfileVo> schoolProfileVos = new ArrayList<>();
        List<Weight> weightList = null;
        SqlParameter sql = SqlParameter.getParameter();
        if (tl.equals("TOEFL")) {
            if (tlScore.equals(">=100")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校 Dream School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70)
                                .addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "100").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMin", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70)
                                .addQuery("apActWeightMin", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "100").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70)
                                .addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校 求具体分数 得出三个学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "100").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70)
                                .addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("99—90")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "99").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70)
                                .addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "99").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "99").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校 求具体分数 得出三个学校 加60分 如果没有 就依次往下减10分
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "99").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("89—79")) {
                //查询小于90的学校
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //根据 查出来的权重id 去查询权重数据
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "89").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "89").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }

                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "89").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "89").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("78—69")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //根据 查出来的权重id 去查询权重数据
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "78").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "78").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "78").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "78").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("68—61")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "68").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "68").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "68").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "68").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("小于61")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "60").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "60").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apctWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "60").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "60").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
        }
        if (tl.equals("IELTS")) {
            if (tlScore.equals("7.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("7")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "7").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("6.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("6")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "6").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("5.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
            if (tlScore.equals("低于5.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0")
                                .getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 60).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0")
                                .getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Dream School");//梦想学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Target School");//目标学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            for (Weight weight : weightList) {
                                Weight weight1 = weightMapper.selectById(weight.getId());
                                SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
                                SchoolProfileVo schoolProfileVo = new SchoolProfileVo();
                                BeanCopierEx.copy(schoolProfile,schoolProfileVo);
                                schoolProfileVo.setDreamSchool("Safety colleges");//保底学校
                                schoolProfileVos.add(schoolProfileVo);
                            }
                        }
                    }
                }
            }
        }
        ajaxResult.put("school", schoolProfileVos);
        return ajaxResult;
    }
}

