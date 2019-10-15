package com.select.school.service.wxApplet.impl;

import com.select.school.mapper.*;
import com.select.school.model.dto.*;
import com.select.school.model.entity.*;
import com.select.school.model.vo.AcceptanceVo;
import com.select.school.model.vo.RadarMapVo;
import com.select.school.model.vo.UserScoreVo;
import com.select.school.utils.DateUtil;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.result.AjaxResult;
import com.select.school.model.vo.SchoolProfileVo;
import com.select.school.service.wxApplet.UserScoreService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserScoresImpl implements UserScoreService {

    private static Logger logger = LoggerFactory.getLogger(UserScoresImpl.class);
    @Autowired
    private UserScoresMapper userScoresMapper;
    @Autowired
    private WeightMapper weightMapper;
    @Autowired
    private SchoolProfileMapper schoolProfileMapper;
    @Autowired
    private SchoolAdmissionScoresMapper schoolAdmissionScoresMapper;
    @Autowired
    private ScoresMapper scoresMapper;

    /**
     * 微信学生选择之后返回数据
     *
     * @param options
     * @return
     */
    public AjaxResult insertOption(List<OptionDTO> options, String openid) {
        AjaxResult ajaxResult = new AjaxResult();
        int num = 0;
        String ielts = "";
        String toefl = "";
        String act_sat = "";
        String ib_ap = "";
        String sex = "";
        String sat = "";
        String ib = "";
        String gpa = "";
        String act = "";
        String rank = "";
        String tf = "";
        String ap = "";
        String one = "";
        String two = "";
        String three = "";
        UserScores userScores = new UserScores();//学生选择问题 总数表
        Scores scores = new Scores();//学校选择问题报错表
        for (OptionDTO option : options) {
            logger.info("\n optionList:" + option.toString());
            int problemId = option.getProblemId();
            //判断问题分数不等于 并且分数不等于0 并且第一题不算入分数中
            String number = option.getNumber();
            if (number != null && !number.equals("0")) {
                // 12 题
                if (problemId == 12) {
                    act_sat = "SAT";
                    sat = option.getOption();
                } else if (problemId == 13) { //13 题
                    act_sat = "ACT";
                    act = option.getOption();
                }
                //15 题
                if (problemId == 15) {
                    ib_ap = "AP";
                    ap = option.getOption();
                } else if (problemId == 16) {
                    ib_ap = "IB";
                    ib = option.getOption();
                }
                int i = Integer.parseInt(number);
                num += i; //总分数
            }
            //第五题
            if (problemId == 5) {
                String seq = option.getSeq();
                if (seq.equals("1")) {
                    one = "1";
                }
                if (seq.equals("2")) {
                    two = "2";
                }
                if (seq.equals("3")) {
                    three = "3";
                }
            }

            //获取性别
            if (problemId == 1) {
                sex = option.getOption();
            }
            //获取排名
            if (problemId == 4) {
                rank = option.getOption();
            }
            //获取gpa
            if (problemId == 6) {
                gpa = option.getOption();
            }
            String tl = option.getTl();
            //选择雅思或者托福成绩
            if (tl != null &&  !"".equals(tl)) {
                //雅思 IELTS
                if (tl.equals("IELTS")) {
                    ielts = option.getOption();
                    logger.info("雅思成绩:"+ielts);
                }
                //托福 TOEFL
                if (tl.equals("TOEFL")) {
                    toefl = option.getOption();
                    tf = option.getOption();
                    logger.info("托福成绩:"+toefl);
                }
            }
        }
        logger.info("ielts=="+ielts);
        logger.info("toefl=="+toefl);
        if (StringUtils.isNotBlank(toefl) && StringUtils.isNotBlank(ielts)) {
            if (toefl.equals(">=100")) {
                // 1 如果 托福 是大于100分 以托福为主。不管雅思多少分
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
        if (toefl == "" &&  !"".equals(ielts)) {
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
        //如果选择第一题 不加不减
        if (StringUtils.isNotBlank(one)){
            userScores.setScores(num);
            //如果选择1和2 不加不减
        }else if(StringUtils.isNotBlank(one) && StringUtils.isNotBlank(two)){
            userScores.setScores(num);
            //如果选择2 减30
        }else if(StringUtils.isBlank(one)&&StringUtils.isNotBlank(two)){
            userScores.setScores(num-30);
            //如果两个都没选择 减60
        }else if (StringUtils.isBlank(one)&&StringUtils.isBlank(two)&&StringUtils.isNotBlank(three)){
            userScores.setScores(num-60);
        }
        userScores.setCreateTime(new Date());
        userScores.setActSat(act_sat);
        userScores.setApIb(ib_ap);
        userScores.setOpenId(openid);
        userScoresMapper.insertList(userScores);
        //根据id 查询数据库 返回一个id 给小程序
        UserScoreVo userScores1 = userScoresMapper.selectId(userScores.getId());
        scores.setUserId(userScores1.getId());
        scores.setActAvg(act);//act 平均
        scores.setApCourse(ap);//ap
        scores.setGpaAvg(gpa);//gpa
        scores.setIbAvg(ib);//ib
        scores.setSatAvgHigh(sat);//sat
        scores.setRank(rank);//排名
        scores.setToeflLow(tf);//toefl
        scoresMapper.insertScores(scores);
        ajaxResult.put("userScores", userScores1);
        return ajaxResult;
    }

    /**
     * 根据学生成绩查询 九所学校
     *
     * @param id
     * @return
     * @
     */
    public AjaxResult selectSchool(int id) {
        AjaxResult ajaxResult = new AjaxResult();
        //根据id 查询成绩
        UserScores userScores = userScoresMapper.selectOpenId(id);
        String sat_act = userScores.getActSat();
        String ib_ap = userScores.getApIb();
        String tl = userScores.getTl();
        String tlScore = userScores.getTlScore();
        List<SchoolProfileVo> schoolProfileVo = new ArrayList<>();
        ReportFileDTO reportFileDTOS = new ReportFileDTO();
        List<Weight> weightList = null;
        SqlParameter sql = SqlParameter.getParameter();
        reportFileDTOS.setPreface("前言：\n" +
                "美国大学的录取标准分为“与生俱来，不可改变”的指标和“后天努力，可提高”的标化指标/成绩和非标化成绩，“与生俱来，不可改变”" +
                "的指标包括国籍，家庭总收入，父母是否是你想申请的大学的毕业，父母教育程度，居住城市，等等。而“后天努力，可提高”的标化指标和" +
                "成绩包括GPA，SAT， ACT， ，AP，IB，TOEFL等成绩，以及非标化成绩：课外活动（体育，学术，社会活动）， 面试表现，论文及推荐信，等等。" +
                "此咨询报告侧重于对学生的“后天努力，可提高”的标化指标/成绩和非标化成绩的分析和建议。");//前言
        reportFileDTOS.setDataModel("理论及数据模型：\n" +
                "首先，真实的数据非常关键，而我们的模型和算法是基于大量的已经成功入学的各个学校的中国留学生的真实数据并比对各个学校的其他公开数据。（附某前30名大学中国留学生的调研数据）\n" +
                "其次，我们运用大数据及人工智能对成千上万的成功案例的结构化数据和非结构化数据进行过滤，分析，挖掘，比对，优化后自动生成了数据模型，然后机器自动让数据模型对各个案例进行不断比对，改进，和学习，最终我们的选校成功率远远领先业界的顾问人工模式。\n" +
                "因各个学校的录取标准和录取率每年都可能会有调整，各个学校的录取标准还需要看“与生俱来，不可改变”的指标，以及论文和推荐信等非量化指标。\n" +
                "最后，此咨询报告是由机器自动生成。\n" +
                "根据您的各项指标的输入，我们分别为您提供了3所学校梦想学校，3所目标学校和3所保底学校的建议名单，指标分析和建议以及次9所大学的录取概率。");//数据模型
        reportFileDTOS.setExplain("\n若有兴趣了解更多，可联系我们的客服----->>>>>>>>>>>>客服微信号\n");
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
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70)
                                .addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMin", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70)
                                .addQuery("apActWeightMin", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70)
                                .addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70)
                                .addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "100").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70)
                                .addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "99").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "89").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "78").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "68").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apctWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "60").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "7").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "6").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.5").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 20)
                                .addQuery("ibActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 70).
                                addQuery("ibActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 20)
                                .addQuery("apActWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 70).
                                addQuery("apActWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 20)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 70).
                                addQuery("ibSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
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
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 20)
                                .addQuery("apSatWeightMax", userScores.getScores() + 20).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 70).
                                addQuery("apSatWeightMax", userScores.getScores() - 30).addQuery("toeflHigh", "5.4").addQuery("toeflLow", "0").getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, schoolProfileVo,userScores);
                        }
                    }
                }
            }
        }
        ajaxResult.put("school", reportFileDTOS);
        return ajaxResult;
    }

    //保底学校
    private void safetyColleges(List<Weight> weightList, ReportFileDTO reportFileDTO, List<SchoolProfileVo> schoolProfileVo,UserScores userScores) {
        List<SafetySchoolDTO> safetySchoolDTOS = new ArrayList<>();
        SchoolProfileVo schoolVo = new SchoolProfileVo();
        List<AcceptanceVo> acceptanceVos = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        if (userScores.getActSat().equals("ACT")){
            act_sat = "ACT";
        }if (userScores.getApIb().equals("AP")){
            ap_ib = "AP";
        }if (userScores.getApIb().equals("IB")){
            ap_ib = "IB";
        }if (userScores.getActSat().equals("SAT")){
            act_sat = "SAT";
        }
        AcceptanceVo acceptanceVo = new AcceptanceVo();//录取率
        List<SafetyAcceptanceDTO> safetyAcceptanceDTOList = new ArrayList<>();
        for (Weight weight : weightList) {

            RadarMapVo radarMapVo = new RadarMapVo();//雷达图
            SafetyAcceptanceDTO safetyAcceptanceDTO = new SafetyAcceptanceDTO();//保底学校数据
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            SafetySchoolDTO safetySchoolDTO = new SafetySchoolDTO();
            //雷达图数据 radarMapVo
            radarMapVo.setActAvgResults(admissionScores.getActAvgResults());
            radarMapVo.setApNumCourse(admissionScores.getApNumCourse());
            radarMapVo.setChGpaAvgStu(admissionScores.getChGpaAvgStu());
            radarMapVo.setIbAvgResults(admissionScores.getIbAvgResults());
            radarMapVo.setSchoolRank(admissionScores.getNineteen());
            radarMapVo.setToeflLowReq(admissionScores.getToeflLowReq());
            radarMapVo.setChSatAvgHighStu(admissionScores.getChSatAvgHighStu());
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            safetySchoolDTO.setDetails("您的TOEFL成绩符合羅格斯大學新布朗斯維克分校录取要求。\n" +
                    "您在AP选课方面与历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩有差距，还需努力！\n" +
                    "您的年级排名，GPA，SAT比历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩高。\n");
            safetySchoolDTO.setChName(schoolProfile.getChName());//学校中文名
            safetySchoolDTO.setSchoolName(schoolProfile.getSchoolName());//学校英文名
            safetySchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());//学校简介
            safetySchoolDTO.setNineteen(admissionScores.getNineteen());//19年排名
            safetySchoolDTO.setTwenty(admissionScores.getTwenty());//20年排名
            safetySchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            safetySchoolDTO.setTuitionFees(admissionScores.getTuitionFees());//学费 美元
            safetySchoolDTO.setSafetySchool("Safety School 保底学校：\n");
            safetySchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            safetySchoolDTO.setNationalStuAccep(national);//国际生录取率
            safetySchoolDTO.setRadarMap(radarMapVo);//保存类雷达图
            safetySchoolDTOS.add(safetySchoolDTO);  // 保底学校数据
           double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            String schoolWeight = null;
            String lastWeight = null;
           //最后权重-学校权重
            if (act_sat.equals("ACT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApAct();
                    //最后权重
                    lastWeight = weight1.getApActWeight();

                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbAct();
                    lastWeight = weight1.getIbActWeight();
                }
            }
            if (act_sat.equals("SAT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApSat();
                    lastWeight = weight1.getApSatWeight();
                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbSat();
                    lastWeight = weight1.getIbSatWeight();
                }
            }
            double schoolWeightInt = Double.valueOf(schoolWeight.toString());
            double lastWeightInt = Double.valueOf(lastWeight.toString());
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100-(lastWeightInt-schoolWeightInt);//(100-（最后权重-学校权重）
            double number = num/numWeight;
            String acc = DateUtil.getPercentFormat(number,2,2);
            safetyAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            safetyAcceptanceDTOList.add(safetyAcceptanceDTO);

        }
        schoolVo.setSafetySchoolDTOS(safetySchoolDTOS);//把保底学校的数据放入list 里面
        acceptanceVo.setSafety(safetyAcceptanceDTOList);
        acceptanceVos.add(acceptanceVo);
        schoolProfileVo.add(schoolVo);
        reportFileDTO.setSchoolProfileVos(schoolProfileVo);
        reportFileDTO.setAcceptance(acceptanceVos);
    }

    //目标学校
    private void targetSchool(List<Weight> weightList, ReportFileDTO reportFileDTO, List<SchoolProfileVo> schoolProfileVo,UserScores userScores) {
        List<TargetSchoolDTO> targetSchoolDTOS = new ArrayList<>();
        SchoolProfileVo schoolVo = new SchoolProfileVo();
        List<AcceptanceVo> acceptanceVos = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        if (userScores.getActSat().equals("ACT")){
            act_sat = "ACT";
        }if (userScores.getApIb().equals("AP")){
            ap_ib = "AP";
        }if (userScores.getApIb().equals("IB")){
            ap_ib = "IB";
        }if (userScores.getActSat().equals("SAT")){
            act_sat = "SAT";
        }
        List<TargetAcceptanceDTO> targetAcceptanceDTOS = new ArrayList<>();
        AcceptanceVo acceptanceVo = new AcceptanceVo();//录取率
        for (Weight weight : weightList) {
            RadarMapVo radarMapVo = new RadarMapVo();//雷达图
            TargetAcceptanceDTO targetAcceptanceDTO = new TargetAcceptanceDTO();
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            TargetSchoolDTO targetSchoolDTO = new TargetSchoolDTO();
            //雷达图数据 radarMapVo
            radarMapVo.setActAvgResults(admissionScores.getActAvgResults());
            radarMapVo.setApNumCourse(admissionScores.getApNumCourse());
            radarMapVo.setChGpaAvgStu(admissionScores.getChGpaAvgStu());
            radarMapVo.setIbAvgResults(admissionScores.getIbAvgResults());
            radarMapVo.setSchoolRank(admissionScores.getNineteen());
            radarMapVo.setToeflLowReq(admissionScores.getToeflLowReq());
            radarMapVo.setChSatAvgHighStu(admissionScores.getChSatAvgHighStu());
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            targetSchoolDTO.setChName(schoolProfile.getChName());
            targetSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            targetSchoolDTO.setDetails("您的TOEFL成绩符合羅格斯大學新布朗斯維克分校录取要求。\n" +
                    "您在AP选课方面与历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩有差距，还需努力！\n" +
                    "您的年级排名，GPA，SAT比历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩高。\n");
            targetSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            targetSchoolDTO.setNineteen(admissionScores.getNineteen());//19年排名
            targetSchoolDTO.setTwenty(admissionScores.getTwenty());//20年排名
            targetSchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            targetSchoolDTO.setTuitionFees(admissionScores.getTuitionFees());//学费 美元
            targetSchoolDTO.setTargetSchool("Target School 目标学校：\n");
            targetSchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            targetSchoolDTO.setNationalStuAccep(national);//国际生录取率
            targetSchoolDTO.setRadarMap(radarMapVo);
            targetSchoolDTOS.add(targetSchoolDTO);
            double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            String schoolWeight = null;
            String lastWeight = null;
            //最后权重-学校权重
            if (act_sat.equals("ACT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApAct();
                    //最后权重
                    lastWeight = weight1.getApActWeight();

                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbAct();
                    lastWeight = weight1.getIbActWeight();
                }
            }
            if (act_sat.equals("SAT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApSat();
                    lastWeight = weight1.getApSatWeight();
                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbSat();
                    lastWeight = weight1.getIbSatWeight();
                }
            }
            double schoolWeightInt = Double.valueOf(schoolWeight.toString());
            double lastWeightInt = Double.valueOf(lastWeight.toString());
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100-(lastWeightInt-schoolWeightInt);//(100-（最后权重-学校权重）
            double number = num/numWeight;
            String acc = DateUtil.getPercentFormat(number,2,2);
            targetAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            targetAcceptanceDTOS.add(targetAcceptanceDTO);
        }
        schoolVo.setTargetSchoolDTOS(targetSchoolDTOS);
        acceptanceVo.setTarget(targetAcceptanceDTOS);
        acceptanceVos.add(acceptanceVo);
        schoolProfileVo.add(schoolVo);
        reportFileDTO.setSchoolProfileVos(schoolProfileVo);
//        reportFileDTO.setAcceptance(acceptanceVos);
    }

    //梦想学校
    private void dreamSchool(List<Weight> weightList, ReportFileDTO reportFileDTO, List<SchoolProfileVo> schoolProfileVo,UserScores userScores) {
        List<DreamSchoolDTO> dreamSchoolDTOS = new ArrayList<>();
        SchoolProfileVo schoolVo = new SchoolProfileVo();
        List<AcceptanceVo> acceptanceVos = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        if (userScores.getActSat().equals("ACT")){
            act_sat = "ACT";
        }if (userScores.getApIb().equals("AP")){
            ap_ib = "AP";
        }if (userScores.getApIb().equals("IB")){
            ap_ib = "IB";
        }if (userScores.getActSat().equals("SAT")){
            act_sat = "SAT";
        }
        List<DreamAcceptanceDTO> dreamAcceptanceDTOS = new ArrayList<>();
        AcceptanceVo acceptanceVo = new AcceptanceVo();
        for (Weight weight : weightList) {
            RadarMapVo radarMapVo = new RadarMapVo();//雷达图
            DreamAcceptanceDTO dreamAcceptanceDTO = new DreamAcceptanceDTO();//录取率
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            DreamSchoolDTO dreamSchoolDTO = new DreamSchoolDTO();
            //雷达图数据 radarMapVo
            radarMapVo.setActAvgResults(admissionScores.getActAvgResults());
            radarMapVo.setApNumCourse(admissionScores.getApNumCourse());
            radarMapVo.setChGpaAvgStu(admissionScores.getChGpaAvgStu());
            radarMapVo.setIbAvgResults(admissionScores.getIbAvgResults());
            radarMapVo.setSchoolRank(admissionScores.getNineteen());
            radarMapVo.setToeflLowReq(admissionScores.getToeflLowReq());
            radarMapVo.setChSatAvgHighStu(admissionScores.getChSatAvgHighStu());
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            dreamSchoolDTO.setChName(schoolProfile.getChName());
            dreamSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            dreamSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            dreamSchoolDTO.setNineteen(admissionScores.getNineteen());//19年排名
            dreamSchoolDTO.setTwenty(admissionScores.getTwenty());//20年排名
            dreamSchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            dreamSchoolDTO.setTuitionFees(admissionScores.getTuitionFees());//学费 美元
            dreamSchoolDTO.setDreamSchool("Dream School 梦想学校：\n");
            dreamSchoolDTO.setDetails("您的TOEFL成绩符合羅格斯大學新布朗斯維克分校录取要求。\n" +
                    "您在AP选课方面与历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩有差距，还需努力！\n" +
                    "您的年级排名，GPA，SAT比历届羅格斯大學新布朗斯維克分校中国留学生的平均成绩高。\n");
            dreamSchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            dreamSchoolDTO.setNationalStuAccep(national);//国际生录取率
            dreamSchoolDTO.setRadarMap(radarMapVo);
            double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            String schoolWeight = null;
            String lastWeight = null;
            //最后权重-学校权重
            if (act_sat.equals("ACT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApAct();
                    //最后权重
                    lastWeight = weight1.getApActWeight();

                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbAct();
                    lastWeight = weight1.getIbActWeight();
                }
            }
            if (act_sat.equals("SAT")){
                if (ap_ib.equals("AP")){
                    //学校权重
                    schoolWeight = admissionScores.getApSat();
                    lastWeight = weight1.getApSatWeight();
                }
                if (ap_ib.equals("IB")){
                    //学校权重
                    schoolWeight = admissionScores.getIbSat();
                    lastWeight = weight1.getIbSatWeight();
                }
            }
            double schoolWeightInt = Double.valueOf(schoolWeight.toString());
            double lastWeightInt = Double.valueOf(lastWeight.toString());
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100-(lastWeightInt-schoolWeightInt);//(100-（最后权重-学校权重）
            double number = num/numWeight;
            String acc = DateUtil.getPercentFormat(number,2,2);
            dreamAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            dreamSchoolDTOS.add(dreamSchoolDTO);//梦想学校
            dreamAcceptanceDTOS.add(dreamAcceptanceDTO);//梦想学校录取率

        }
        schoolVo.setDreamSchoolDTOS(dreamSchoolDTOS);
        schoolProfileVo.add(schoolVo);
        acceptanceVo.setDream(dreamAcceptanceDTOS); // 把梦想学校录取率 放到集合里
        acceptanceVos.add(acceptanceVo);
        reportFileDTO.setAcceptance(acceptanceVos);//保存录取率
        reportFileDTO.setSchoolProfileVos(schoolProfileVo);//保存学校详情
    }
}

