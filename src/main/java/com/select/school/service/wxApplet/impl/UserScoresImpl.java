package com.select.school.service.wxApplet.impl;

import com.select.school.mapper.*;
import com.select.school.model.dto.*;
import com.select.school.model.entity.*;
import com.select.school.model.vo.*;
import com.select.school.utils.DateUtil;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.result.AjaxResult;
import com.select.school.service.wxApplet.UserScoreService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        int ninthing = 0;
        int twenty = 0;
        UserScores userScores = new UserScores();//学生选择问题 总数表
        logger.info("\n optionList::::" + options.size());
        Scores scores = new Scores();//学校选择问题报错表
        SchoolProfile schoolProfile;
        for (OptionDTO option : options) {
            logger.info("\n optionList:" + option.toString());
            String problemId = option.getProblemId();
            //判断问题分数不等于 并且分数不等于0 并且第一题不算入分数中
            String number = option.getNumber();
            if (number != null && !number.equals("0") && !number.equals("")) {
                // 13 题
                if (problemId.equals("14")) {
                    act_sat = "SAT";
                    sat = option.getNumber();
                } else if (problemId.equals("15")) { //13 题
                    act_sat = "ACT";
                    act = option.getOption();
                }
                //15 题
                if (problemId.equals("17")) {
                    ib_ap = "AP";
                    ap = option.getNumber();
                } else if (problemId.equals("18")) {
                    ib_ap = "IB";
                    ib = option.getOption();
                }
                int i = Integer.parseInt(number);
                num += i; //总分数
            }
            if (problemId.equals("16")) {
                //如果第15题选了第三题 则保存ap成绩
                int seq = option.getSeq();
                if (seq == 3) {
                    ib_ap = "AP";
                    ap = "0";
                }
            }
            //第6题
            if (problemId.equals("6")) {
                int seq = option.getSeq();
                if (seq == 1) {
                    one = "1";
                }
                if (seq == 2) {
                    two = "2";
                }
                if (seq == 3) {
                    three = "3";
                }
            }
            //第十九题
            if (problemId.equals("19")) {
                int seq = option.getSeq();
                if (seq == 1) {
                    ninthing = 70000;
                }
                if (seq == 2) {
                    ninthing = 50000;
                }
                if (seq == 3) {
                    ninthing = 40000;
                }
                if (seq == 4) {
                    ninthing = 30000;
                }
            }
            //第十九题
            if (problemId.equals("20")) {
                int seq = option.getSeq();
                if (seq == 1) {
                    twenty = 0;
                }
                if (seq == 2) {
                    twenty = 10;
                }
                if (seq == 3) {
                    twenty = 50;
                }
            }

            //获取性别
            if (problemId.equals("1")) {
                sex = option.getOption();
            }
            //获取排名
            if (problemId.equals("5")) {
                rank = option.getNumber();
            }
            //获取gpa
            if (problemId.equals("7")) {
                gpa = option.getNumber();
            }
            String tl = option.getTl();
            //选择雅思或者托福成绩
            if (tl != null && !"".equals(tl)) {
                //雅思 IELTS
                if (tl.equals("IELTS")) {
                    ielts = option.getOption();
                    logger.info("雅思成绩:" + ielts);
                }
                //托福 TOEFL
                if (tl.equals("TOEFL")) {
                    toefl = option.getOption();
                    tf = option.getOption();
                    logger.info("托福成绩:" + toefl);
                }
            }
        }
        if (StringUtils.isNotBlank(toefl) && StringUtils.isNotBlank(ielts)) {
            // TODO 判断的数据都是绝对数值  禁止数据库有其他字 或者符号空格一类
            if (toefl.equals(">=100")) {
                // 1 如果 托福 是大于100分 以托福为主。不管雅思多少分
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTl(2); //雅思是1  托福 是2
                    userScores.setTlScore(toefl);
                }
            }
            if (toefl.equals("99----90")) {
                // 2 如果 托福 是99-90分 而 雅思是 7.5以上  以雅思为主 否则以托福为主。
                if (ielts.equals("7.5")) {
                    userScores.setTl(1);
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTl(2);
                    userScores.setTlScore(toefl);
                }
            }

            if (toefl.equals("89----79")) {
                // 3 如果 托福 是89----79分 而雅思是 7以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7")) {
                    userScores.setTl(1);
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTl(2);
                    userScores.setTlScore(toefl);
                }
            }
            if (toefl.equals("78----69")) {
                // 4 如果 托福 是78----69分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5")) {
                    userScores.setTl(1);
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl(2);
                }
            }
            if (toefl.equals("68----61")) {
                // 5 如果 托福 是68----61分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6")) {
                    userScores.setTl(1);
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("5.5") || ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl(2);
                }
            }
            if (toefl.equals("小于61")) {
                if (ielts.equals("低于5.5") || ielts.equals("未考")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl(2);
                }
            }
            if (toefl.equals("未考")) {
                if (ielts.equals("7.5") || ielts.equals("7") || ielts.equals("6.5") || ielts.equals("6") || ielts.equals("5.5") || ielts.equals("低于5.5")) {
                    userScores.setTl(1);
                    userScores.setTlScore(ielts);
                }
                if (ielts.equals("未考")) {
                    userScores.setTlScore(toefl);
                    userScores.setTl(2);
                }
            }
        }
        //如果 托福和雅思都没有成绩 则 成绩为0 没有学习
        if ("未考".equals(ielts) && "未考".equals(toefl)) {
            userScores.setTlScore("0");
            userScores.setTl(2);
        }
        //雅思没考 则保存托福成绩
        if (!"未考".equals(ielts) && "未考".equals(toefl)) {
            userScores.setTlScore(ielts);
            userScores.setTl(1);
        }
        //托福没考 则保存雅思成绩
        if ("未考".equals(ielts) && !"未考".equals(toefl)) {
            userScores.setTlScore(toefl);
            userScores.setTl(2);
        }
        if (sex.equals("女")) {
            //因为本来分数是已经加到一起的 所以 如果小于500就减去原有的10分
            //如果是女生 ap 成绩大于等于500 加10分
            if (ib_ap.equals("AP") && num < 500) {
                num = num - 10;
            }
            //ib成绩大于等于540 加十分
            if (ib_ap.equals("IB") && num < 540) {
                num = num - 10;
            }
        }
        //如果选择第一题 不加不减
        if (StringUtils.isNotBlank(one)) {
            if (num >= 580) {
                num = num + 20;
            }
            userScores.setScores(num);
            userScores.setSixQuestion(one);
            //如果选择1和2 不加不减
        } else if (StringUtils.isNotBlank(one) && StringUtils.isNotBlank(two)) {
            if (num >= 500) {
                num = num + 10;
            }
            if (num >= 580) {
                num = num + 20;
            }
            if (num >= 445) {
                num = num + 5;
            }
            userScores.setScores(num);
            userScores.setSixQuestion("4");//既选择第一个选项 又选择第二个选项 存 4  用于后面给学校加分
            //如果选择2 减30
        } else if (StringUtils.isBlank(one) && StringUtils.isNotBlank(two)) {
            if (num >= 500) {
                num = num + 10;
            }
            if (num >= 445) {
                num = num + 5;
            }
            userScores.setScores(num - 30);
            userScores.setSixQuestion(two);
            //如果两个都没选择 减60
        } else if (StringUtils.isBlank(one) && StringUtils.isBlank(two) && StringUtils.isNotBlank(three)) {
            userScores.setScores(num);
            userScores.setSixQuestion("美国大学招生时比对您所在学校以往的录取记录，由于您的高中上两届毕业生中没有顶尖名校的录取记录，您得加倍努力。");
        }
        userScores.setCreateTime(new Date());
        userScores.setActSat(act_sat);
        userScores.setApIb(ib_ap);
        userScores.setOpenId(openid);
        userScores.setTuitionFees(ninthing);
        userScores.setRequired(twenty);
        userScoresMapper.insertList(userScores);
        //根据id 查询数据库 返回一个id 给小程序
        UserScoreVo userScores1 = userScoresMapper.selectId(userScores.getId());
        scores.setUserScoreId(userScores1.getId());
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
        int tl = userScores.getTl();//托福或雅思
        String question = "";
        String q = userScores.getSixQuestion();
        if (!q.equals("2") && !q.equals("1")){
            question = userScores.getSixQuestion();
        }
        if (tl == 0) {
            return AjaxResult.success("因为英语成绩太低，在全美前300名的学校中无匹配梦想，目标和保底学校。");
        }
        String tlScore = userScores.getTlScore();//托福成绩
        int free = userScores.getTuitionFees();// 学费范围
        int required = userScores.getRequired();// 大一入学新生数
        ReportFileDTO reportFileDTOS = new ReportFileDTO();
        SchoolProfileVo schoolProfileVo = new SchoolProfileVo();//学校详情
        AcceptanceVo acceptanceVo = new AcceptanceVo();//录取率
        List<Weight> weightList = null;
        SqlParameter sql = SqlParameter.getParameter();
        reportFileDTOS.setPreface("前言：\n" +
                "\t\t美国大学的录取标准分为“与生俱来，不可改变”的指标和“后天努力，可提高”的标化指标/成绩和非标化成绩，“与生俱来，不可改变”" +
                "的指标包括国籍，家庭总收入，父母是否是你想申请的大学的毕业，父母教育程度，居住城市，等等。而“后天努力，可提高”的标化指标和" +
                "成绩包括GPA，SAT， ACT， ，AP，IB，TOEFL等成绩，以及非标化成绩：课外活动（体育，学术，社会活动）， 面试表现，论文及推荐信，等等。" +
                "此咨询报告侧重于对学生的“后天努力，可提高”的标化指标/成绩和非标化成绩的分析和建议。");//前言
        reportFileDTOS.setDataModel("理论及数据模型：\n" +
                " \t \t首先，真实的数据非常关键，而我们的模型和算法是基于大量的已经成功入学的各个学校的中国留学生的真实数据并比对各个学校的其他公开数据。（附某前30名大学中国留学生的调研数据）\n" +
                "其次，我们运用大数据及人工智能对成千上万的成功案例的结构化数据和非结构化数据进行过滤，分析，挖掘，比对，优化后自动生成了数据模型，然后机器自动让数据模型对各个案例进行不断比对，改进，和学习，最终我们的选校成功率远远领先业界的顾问人工模式。\n" +
                "因各个学校的录取标准和录取率每年都可能会有调整，各个学校的录取标准还需要看“与生俱来，不可改变”的指标，以及论文和推荐信等非量化指标。\n" +
                "最后，此咨询报告是由机器自动生成。\n" +
                " \t \t根据您的各项指标的输入，我们分别为您提供了3所学校梦想学校，3所目标学校和3所保底学校的建议名单，指标分析和建议以及次9所大学的录取概率。");//数据模型
        reportFileDTOS.setQuestion("\t你的学校的录取概率是x%, 简单地说就是100个和你同样水平的中国学生申请，只有x个学生会被录取。"+"\n\t"+question);
        reportFileDTOS.setExplain("\n若有兴趣了解更多，可联系我们的客服 >>>>>>>>>>>>客服微信号\n");
        if (tl == 2) {
            if (tlScore.equals(">=100")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校 Dream School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",100).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",100).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMin", userScores.getScores() + 30).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMin", userScores.getScores() - 50).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",100).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校 求具体分数 得出三个学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",100).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",100).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("99----90")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",99).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",99).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",99).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校 求具体分数 得出三个学校 加60分 如果没有 就依次往下减10分
                        weightList = weightMapper.selectIbActWeightDream(
                                sql.addQuery("apSatWeightMin", 0)
                                        .addQuery("apSatWeightMax", userScores.getScores() + 70)
                                        .addQuery("toeflHigh",99).addQuery("toeflLow",0)
                                        .addQuery("tuitionFees", free)
                                        .addQuery("required", required)
                                        .addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",99).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("89----79")) {
                //查询小于90的学校
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //根据 查出来的权重id 去查询权重数据
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",89).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",89).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }

                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",89).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",89).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",89).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("78----69")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //根据 查出来的权重id 去查询权重数据
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",79).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",79)
                                .addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",79).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMin", userScores.getScores() - 50).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",79).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",79).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {
                            if (weightList.size() >= 3) {
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",79).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("68----61")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",69).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",69).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() <= 0){
                            weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                    .addQuery("apSatWeightMax", userScores.getScores() + 90).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                    .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        }
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",69).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",69).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",69).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("小于61")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",60).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",60).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",60).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh",60).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh",60).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
        }
        if (tl == 1) {
            if (tlScore.equals("7.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("7")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 7).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 7).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("6.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("6")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 6).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 6).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("5.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.5).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
            if (tlScore.equals("低于5.5")) {
                if (sat_act.equals("ACT")) {
                    if (ib_ap.equals("IB")) {//act ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 30)
                                .addQuery("ibActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                                .addQuery("ibActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0).
                                addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//act ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 30)
                                .addQuery("apActWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                                .addQuery("apActWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0).
                                addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
                if (sat_act.equals("SAT")) {
                    if (ib_ap.equals("IB")) {//sat ib
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 30)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        //如果大于三所学校 就取前三所。如果没有三所学校，n那么查出几所学校就取几所学校
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                                .addQuery("ibSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0).
                                addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                    if (ib_ap.equals("AP")) {//sat ap
                        //梦想学校
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 30)
                                .addQuery("apSatWeightMax", userScores.getScores() + 70).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0)
                                .addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //梦想学校
                            dreamSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //目标学校 Target School
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                                .addQuery("apSatWeightMax", userScores.getScores() + 30).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //目标学校
                            targetSchool(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                        //保底学校 Safety colleges
                        weightList = weightMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0).
                                addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", 5.4).addQuery("toeflLow",0).addQuery("tuitionFees", free).addQuery("required", required).addQuery("tl", tl).getMap());
                        if (weightList.size() > 0) {//如果大于0
                            if (weightList.size() >= 3) {
                                //如果大于3 需要截取前三所学校
                                weightList = weightList.subList(0, 3);
                            }
                            //保底学校
                            safetyColleges(weightList, reportFileDTOS, userScores, schoolProfileVo, acceptanceVo);
                        }
                    }
                }
            }
        }
        ajaxResult.put("school", reportFileDTOS);
        return ajaxResult;
    }

    //保底学校
    private void safetyColleges(List<Weight> weightList, ReportFileDTO reportFileDTO, UserScores userScores, SchoolProfileVo schoolVo, AcceptanceVo acceptanceVo) {
        List<SafetySchoolDTO> safetySchoolDTOS = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        String question = "";
        if (userScores.getSixQuestion().equals("2")){
            question = "2";
        }else if (userScores.getSixQuestion().equals("1")) {
            question = "1";
        }
        if (userScores.getActSat().equals("ACT")) {
            act_sat = "ACT";
        }
        if (userScores.getActSat().equals("ACT")) {
            act_sat = "ACT";
        }
        if (userScores.getApIb().equals("AP")) {
            ap_ib = "AP";
        }
        if (userScores.getApIb().equals("IB")) {
            ap_ib = "IB";
        }
        if (userScores.getActSat().equals("SAT")) {
            act_sat = "SAT";
        }
        List<SafetyAcceptanceDTO> safetyAcceptanceDTOList = new ArrayList<>();
        for (Weight weight : weightList) {
            SafetyAcceptanceDTO safetyAcceptanceDTO = new SafetyAcceptanceDTO();//保底学校数据
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            SafetySchoolDTO safetySchoolDTO = new SafetySchoolDTO();
            //雷达图数据 radarMapVo
            RadarMapVo radarMapVo = radarMap(admissionScores);
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            String detail = selectDetailsSafety(admissionScores, userScores, schoolProfile);
            safetySchoolDTO.setDetails(detail);
            safetySchoolDTO.setChName(schoolProfile.getChName());//学校中文名
            safetySchoolDTO.setSchoolName(schoolProfile.getSchoolName());//学校英文名
            safetySchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());//学校简介
            String nineteen = String.valueOf(admissionScores.getNineteen());
            String twenty = String.valueOf(admissionScores.getTwenty());
            String tuitionfees = String.valueOf(admissionScores.getTuitionFees());
            safetySchoolDTO.setNineteen(nineteen);//19年排名
            safetySchoolDTO.setCrest(schoolProfile.getCrest());
            safetySchoolDTO.setTwenty(twenty);//20年排名
            safetySchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            safetySchoolDTO.setTuitionFees(tuitionfees);//学费 美元
            safetySchoolDTO.setSafetySchool("Safety School 保底学校：\n");
            safetySchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            safetySchoolDTO.setNationalStuAccep(national);//国际生录取率
            safetySchoolDTO.setRadarMap(radarMapVo);//保存类雷达图
            safetySchoolDTOS.add(safetySchoolDTO);  // 保底学校数据
            double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            int schoolWeight = 0;
            int lastWeight = 0;
            SchoolWeightVo schoolWeightVo = null;
            if (question.equals("1") || question.equals("2")) {
                schoolWeightVo = weightVo(admissionScores, schoolProfile, schoolWeight);
                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApAct() == 0){
                            schoolWeight = admissionScores.getApAct();
                        }else{
                            schoolWeight = schoolWeightVo.getApAct();
                        }
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbAct() == 0) {
                            schoolWeight = admissionScores.getIbAct();
                        }else{
                            schoolWeight = schoolWeightVo.getIbAct();
                        }
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApSat() == 0) {
                            schoolWeight = admissionScores.getApSat();
                        }else {
                            schoolWeight = schoolWeightVo.getApSat();
                        }
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbSat() == 0) {
                            schoolWeight = admissionScores.getIbSat();
                        }else {
                            schoolWeight = schoolWeightVo.getIbSat();
                        }
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }else if(question.equals("")) {

                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApAct();
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbAct();
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApSat();
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbSat();
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100 - (lastWeight - schoolWeight);//(100-（最后权重-学校权重）
            double number = num / numWeight;
            String acc = DateUtil.getPercentFormat(number, 2, 2);
            safetySchoolDTO.setAcceptance(acc);//录取率
            safetyAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            safetyAcceptanceDTOList.add(safetyAcceptanceDTO);
        }
        schoolVo.setSafetySchoolDTOS(safetySchoolDTOS);//把保底学校的数据放入list 里面
        acceptanceVo.setSafety(safetyAcceptanceDTOList);
        reportFileDTO.setSchoolProfileVos(schoolVo);
        reportFileDTO.setAcceptance(acceptanceVo);
    }

    //目标学校
    private void targetSchool(List<Weight> weightList, ReportFileDTO reportFileDTO, UserScores userScores, SchoolProfileVo schoolProfileVo, AcceptanceVo acceptanceVo) {
        List<TargetSchoolDTO> targetSchoolDTOS = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        String question = "";
        if (userScores.getSixQuestion().equals("2")){
            question = "2";
        }else if (userScores.getSixQuestion().equals("1")) question = "1";
        if (userScores.getActSat().equals("ACT")) {
            act_sat = "ACT";
        }
        if (userScores.getActSat().equals("ACT")) {
            act_sat = "ACT";
        }
        if (userScores.getApIb().equals("AP")) {
            ap_ib = "AP";
        }
        if (userScores.getApIb().equals("IB")) {
            ap_ib = "IB";
        }
        if (userScores.getActSat().equals("SAT")) {
            act_sat = "SAT";
        }
        List<TargetAcceptanceDTO> targetAcceptanceDTOS = new ArrayList<>();
        for (Weight weight : weightList) {
            TargetAcceptanceDTO targetAcceptanceDTO = new TargetAcceptanceDTO();
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            TargetSchoolDTO targetSchoolDTO = new TargetSchoolDTO();
            //雷达图数据 radarMapVo
            RadarMapVo radarMapVo = radarMap(admissionScores);
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            targetSchoolDTO.setChName(schoolProfile.getChName());
            targetSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            String detail = selectDetailsSafety(admissionScores, userScores, schoolProfile);
            targetSchoolDTO.setDetails(detail);
            targetSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            String nineteen = String.valueOf(admissionScores.getNineteen());
            String twenty = String.valueOf(admissionScores.getTwenty());
            String tuitionfees = String.valueOf(admissionScores.getTuitionFees());
            targetSchoolDTO.setNineteen(nineteen);//19年排名
            targetSchoolDTO.setTwenty(twenty);//20年排名
            targetSchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            targetSchoolDTO.setTuitionFees(tuitionfees);//学费 美元
            targetSchoolDTO.setCrest(schoolProfile.getCrest());
            targetSchoolDTO.setTargetSchool("Target School 目标学校：\n");
            targetSchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            targetSchoolDTO.setNationalStuAccep(national);//国际生录取率
            targetSchoolDTO.setRadarMap(radarMapVo);
            targetSchoolDTOS.add(targetSchoolDTO);
            double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            int schoolWeight = 0;
            int lastWeight = 0;
            SchoolWeightVo schoolWeightVo = null;
            if (question.equals("1") || question.equals("2")) {
                schoolWeightVo = weightVo(admissionScores, schoolProfile, schoolWeight);
                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApAct() == 0){
                            schoolWeight = admissionScores.getApAct();
                        }else{
                            schoolWeight = schoolWeightVo.getApAct();
                        }
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbAct() == 0) {
                            schoolWeight = admissionScores.getIbAct();
                        }else{
                            schoolWeight = schoolWeightVo.getIbAct();
                        }
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApSat() == 0) {
                            schoolWeight = admissionScores.getApSat();
                        }else {
                            schoolWeight = schoolWeightVo.getApSat();
                        }
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbSat() == 0) {
                            schoolWeight = admissionScores.getIbSat();
                        }else {
                            schoolWeight = schoolWeightVo.getIbSat();
                        }
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }else if(question.equals("")) {
                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApAct();
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbAct();
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApSat();
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbSat();
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100 - (lastWeight - schoolWeight);//(100-（最后权重-学校权重）
            double number = num / numWeight;
            String acc = DateUtil.getPercentFormat(number, 2, 2);
            targetSchoolDTO.setAcceptance(acc);
            targetAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            targetAcceptanceDTOS.add(targetAcceptanceDTO);
        }
        schoolProfileVo.setTargetSchoolDTOS(targetSchoolDTOS);
        acceptanceVo.setTarget(targetAcceptanceDTOS);
        reportFileDTO.setSchoolProfileVos(schoolProfileVo);
        reportFileDTO.setAcceptance(acceptanceVo);
    }

    //梦想学校
    private void dreamSchool(List<Weight> weightList, ReportFileDTO reportFileDTO, UserScores userScores, SchoolProfileVo schoolVo, AcceptanceVo acceptanceVo) {
        List<DreamSchoolDTO> dreamSchoolDTOS = new ArrayList<>();
        String act_sat = "";
        String ap_ib = "";
        String question = "";
        if (userScores.getSixQuestion().equals("2")){
            question = "2";
        }else if (userScores.getSixQuestion().equals("1")) question = "1";
        if (userScores.getActSat().equals("ACT")) {
            act_sat = "ACT";
        }
        if (userScores.getApIb().equals("AP")) {
            ap_ib = "AP";
        }
        if (userScores.getApIb().equals("IB")) {
            ap_ib = "IB";
        }
        if (userScores.getActSat().equals("SAT")) {
            act_sat = "SAT";
        }
        List<DreamAcceptanceDTO> dreamAcceptanceDTOS = new ArrayList<>();
        for (Weight weight : weightList) {
            DreamAcceptanceDTO dreamAcceptanceDTO = new DreamAcceptanceDTO();//录取率
            Weight weight1 = weightMapper.selectById(weight.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(weight1.getSchoolId());
            SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolProfile.getId());
            DreamSchoolDTO dreamSchoolDTO = new DreamSchoolDTO();
            //雷达图数据 radarMapVo
            RadarMapVo radarMapVo = radarMap(admissionScores);
            String national = admissionScores.getNationalStuAccep();
            //学校详情数据
            dreamSchoolDTO.setChName(schoolProfile.getChName());
            dreamSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            dreamSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            String nineteen = String.valueOf(admissionScores.getNineteen());
            String twenty = String.valueOf(admissionScores.getTwenty());
            String tuitionfees = String.valueOf(admissionScores.getTuitionFees());
            dreamSchoolDTO.setNineteen(nineteen);//19年排名
            dreamSchoolDTO.setTwenty(twenty);//20年排名
            dreamSchoolDTO.setSchoolRank(schoolProfile.getSchoolRank());//学校排名
            dreamSchoolDTO.setTuitionFees(tuitionfees);//学费 美元
            dreamSchoolDTO.setCrest(schoolProfile.getCrest());
            dreamSchoolDTO.setDreamSchool("Dream School 梦想学校：\n");
            String detail = selectDetailsSafety(admissionScores, userScores, schoolProfile);
            dreamSchoolDTO.setDetails(detail);
            dreamSchoolDTO.setNumNationalFreshmen(admissionScores.getNumNationalFreshmen());//大一国际生人数
            dreamSchoolDTO.setNationalStuAccep(national);//国际生录取率
            dreamSchoolDTO.setRadarMap(radarMapVo);
            double n = DateUtil.stringToDouble(national);//把录取率转成double类型 用来计算
            int schoolWeight = 0;
            int lastWeight = 0;
            SchoolWeightVo schoolWeightVo = null;
            if (question.equals("1") || question.equals("2")) {
                schoolWeightVo = weightVo(admissionScores, schoolProfile, schoolWeight);
                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApAct() == 0){
                            schoolWeight = admissionScores.getApAct();
                        }else{
                            schoolWeight = schoolWeightVo.getApAct();
                        }
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbAct() == 0) {
                            schoolWeight = admissionScores.getIbAct();
                        }else{
                            schoolWeight = schoolWeightVo.getIbAct();
                        }
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        if (schoolWeightVo.getApSat() == 0) {
                            schoolWeight = admissionScores.getApSat();
                        }else {
                            schoolWeight = schoolWeightVo.getApSat();
                        }
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        if (schoolWeightVo.getIbSat() == 0) {
                            schoolWeight = admissionScores.getIbSat();
                        }else {
                            schoolWeight = schoolWeightVo.getIbSat();
                        }
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }else if(question.equals("")) {
                if (act_sat.equals("ACT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApAct();
                        lastWeight = weight1.getApActWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbAct();
                        lastWeight = weight1.getIbActWeight();
                    }
                }
                if (act_sat.equals("SAT")) {
                    if (ap_ib.equals("AP")) {
                        schoolWeight = admissionScores.getApSat();
                        lastWeight = weight1.getApSatWeight();
                    }
                    if (ap_ib.equals("IB")) {
                        schoolWeight = admissionScores.getIbSat();
                        lastWeight = weight1.getIbSatWeight();
                    }
                }
            }
            double num = n * 100;  //国际生录取率*100
            double numWeight = 100 - (lastWeight - schoolWeight);//(100-（最后权重-学校权重）
            double number = num / numWeight;
            String acc = DateUtil.getPercentFormat(number, 2, 2);
            dreamSchoolDTO.setAcceptance(acc);//录取率
            dreamAcceptanceDTO.setAcceptance(acc);//添加保底学校的录取率 录取率=国际生录取率*100/(100-（最后权重-学校权重）
            dreamSchoolDTOS.add(dreamSchoolDTO);//梦想学校
            dreamAcceptanceDTOS.add(dreamAcceptanceDTO);//梦想学校录取率
        }
        schoolVo.setDreamSchoolDTOS(dreamSchoolDTOS);
        reportFileDTO.setSchoolProfileVos(schoolVo);//保存学校详情
        acceptanceVo.setDream(dreamAcceptanceDTOS); // 把梦想学校录取率 放到集合里
        reportFileDTO.setAcceptance(acceptanceVo);//保存录取率
    }


    /**
     * 第六题 根据选择的学校加分数
     * @param admissionScores
     * @param schoolProfile
     * @param schoolWeight
     * @return
     */
    private SchoolWeightVo weightVo(SchoolAdmissionScores admissionScores, SchoolProfile schoolProfile, int schoolWeight){
        SchoolWeightVo schoolWeightVo = new SchoolWeightVo();
        int id = schoolProfile.getId();
        int sat_ap = admissionScores.getApSat();
        int sat_ib = admissionScores.getIbSat();
        int act_ap = admissionScores.getApAct();
        int act_ib = admissionScores.getIbAct();
        // 如果有这些大学 权重加20分
        if (id == 1){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 2){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 4){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 5){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 7){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 6){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 3){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 8){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 9){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 11){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 10){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 13){
            sat_ap = sat_ap + 20;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 20;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 20;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 20;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 14){ //以下学校加10分
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 16){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 19){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 17){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 12){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 15){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 18){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 23){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 21){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 20){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 22){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 24){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }else if(id == 25){
            sat_ap = sat_ap + 10;
            schoolWeightVo.setApSat(sat_ap);
            sat_ib = sat_ib + 10;
            schoolWeightVo.setIbSat(sat_ib);
            act_ap = act_ap + 10;
            schoolWeightVo.setApAct(act_ap);
            act_ib = act_ib + 10;
            schoolWeightVo.setIbAct(act_ib);
            return schoolWeightVo;
        }
        return schoolWeightVo;
    }

    /**
     * 获取雷达图数据
     *
     * @param admissionScores
     * @return
     */
    private RadarMapVo radarMap(SchoolAdmissionScores admissionScores) {
        RadarMapVo radarMapVo = new RadarMapVo();
        //雷达图数据 radarMapVo
        String act = admissionScores.getActWeight();
        if (!act.equals("") && act != null) {
            int i = Integer.parseInt(act);
            int percentInter = DateUtil.getPercentInter(i, 200);
            radarMapVo.setActAvgResults(percentInter);//传给前端百分比数据
        }
        String ap = admissionScores.getApWeight();
        if (!ap.equals("") && ap != null) {
            int i = Integer.parseInt(ap);//把录取率转成int类型 用来计算
            int percentInter = DateUtil.getPercentInter(i, 200);
            radarMapVo.setApNumCourse(percentInter);
        }
        String gpa = admissionScores.getChGpaWeightStu();
        if (!gpa.equals("") && gpa != null) {
            int i = Integer.parseInt(gpa);//把录取率转成int类型 用来计算
            int percentInter = DateUtil.getPercentInter(i, 200);
            radarMapVo.setChGpaAvgStu(percentInter);//去除百分号
        }
        String ib = admissionScores.getIbWeight();
        if (!ib.equals("") && ib != null) {
            int i = Integer.parseInt(ib);//把录取率转成int类型 用来计算
            int percentInter = DateUtil.getPercentInter(i, 200);
            radarMapVo.setIbAvgResults(percentInter);
        }

        int rank = admissionScores.getNineteen();//排名
        if (rank >= 0){
            int percentInter = DateUtil.getPercentInter(rank, 300);
            radarMapVo.setSchoolRank(101 - percentInter);
        }

        int tolfl = admissionScores.getToeflLowReq();//托福成绩
        if (tolfl >= 0) {
            int percentInter = DateUtil.getPercentInter(tolfl, 100);
            radarMapVo.setToeflLowReq(percentInter);
        }

        String sat = admissionScores.getSatWeight();//sat成绩
        if (!sat.equals("") && sat != null) {
            int i = Integer.parseInt(sat);//把录取率转成int类型 用来计算
            int percentInter = DateUtil.getPercentInter(i, 200);
            radarMapVo.setChSatAvgHighStu(percentInter);
        }
        logger.info("雷达图数据：" + radarMapVo.toString());
        return radarMapVo;
    }


    /**
     * 判断学校和自己分数的差距
     * @param admissionScores
     * @param userScores
     * @param schoolProfile
     * @return
     */
    private String selectDetailsSafety(SchoolAdmissionScores admissionScores, UserScores userScores, SchoolProfile schoolProfile) {
        String schoolName = schoolProfile.getChName();//学校名称
        int userid = userScores.getId();
        Scores scores = scoresMapper.findByUserScoreId(userid);
        int ap = Integer.parseInt(admissionScores.getApWeight());//ap 学校选课权重
        String scoreAp = scores.getApCourse();
        int ap_score = 0;
        if (scoreAp != null && !scoreAp.equals("")) {
            ap_score = Integer.parseInt(scoreAp);
        }
        String scoreGpa = scores.getGpaAvg();
        int gpa_score = 0;
        if (scoreGpa != null && !scoreGpa.equals("")) {
            gpa_score = Integer.parseInt(scoreGpa);
        }
        int gpa = Integer.parseInt(admissionScores.getChGpaWeightStu());
        int act = Integer.parseInt(admissionScores.getActAvgResults());
        String scoreAct = scores.getActAvg();
        int act_score = 0;
        if (scoreAct != null && !scoreAct.equals("")) {
            act_score = Integer.parseInt(scoreAct);
        }
        int ib = Integer.parseInt(admissionScores.getIbAvgResults());
        int ib_score = 0;
        String scoreIb = scores.getIbAvg();
        if (!scoreIb.equals("") && scoreIb != null) {
            ib_score = Integer.parseInt(scoreIb);
        }
        int sat = Integer.parseInt(admissionScores.getSatWeight());
        String scoreSat = scores.getSatAvgHigh();
        int sat_score = 0;
        if (scoreSat != null && !scoreSat.equals("")) {
            sat_score = Integer.parseInt(scoreSat);
        }
        int rank = Integer.parseInt(admissionScores.getChStuWeightRank());//中国学生排名权重
        int rank_score = Integer.parseInt(scores.getRank());
        int toefl_score = userScores.getTl();//托福 选择的成绩
        String tf_detail = "";
        if (toefl_score == 2) {
            tf_detail = "您的TOEFL成绩符合" + schoolName + "录取要求。\n";
        }
        if (toefl_score == 1) {
            tf_detail = "您的IELTS成绩符合" + schoolName + "录取要求。\n";
        }
        String low_detail = "";
        String high_detail = "";
        String same_detail = "";
        if (scoreAp != null && !scoreAp.equals("")) {
            //SAT AP
            if (scoreSat != null && !scoreSat.equals("")) {
                if (ap > ap_score) {
                    low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                    if (gpa > gpa_score) {
                        low_detail = "您在GPA,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (sat > sat_score) {
                            low_detail = "您在GPA,SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                            low_detail = "您在GPA,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            low_detail = "您在GPA,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa == gpa_score) {
                        if (sat > sat_score) {
                            low_detail = "您在SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                            low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa < gpa_score) {
                        if (sat > sat_score) {
                            low_detail = "您在SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩高\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                            low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩高\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                } else if (ap == ap_score) {
                    same_detail = "您在AP选课上和" + schoolName + "中国留学生的平均成绩一致。";
                    if (gpa > gpa_score) {
                        low_detail = "您在GPA,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (sat > sat_score) {
                            low_detail = "您在GPA,SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT，AP选课上和" + schoolName + "中国留学生的平均成绩一致。";
                            low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT，AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            low_detail = "您在GPA,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa < gpa_score) {
                        high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        low_detail = "您在AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        if (sat > sat_score) {
                            low_detail = "您在GPA,SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT,AP选课上和" + schoolName + "中国留学生的平均成绩一致。";
                            high_detail = "您的GPA方面比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            same_detail = "您的AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            high_detail = "您的GPA,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa == gpa_score) {
                        if (sat > sat_score) {
                            low_detail = "您在SAT,AP选课方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            same_detail = "您的GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT,AP,GPA选课上和" + schoolName + "中国留学生的平均成绩一致。";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            same_detail = "您的GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                } else if (ap < ap_score) {
                    if (gpa > gpa_score) {
                        low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (sat > sat_score) {
                            low_detail = "您在GPA,SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                            low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT，AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名，AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa == gpa_score) {
                        same_detail = "您的GPA选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (sat > sat_score) {
                            low_detail = "您在SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT,GPA上和" + schoolName + "中国留学生的平均成绩一致。";
                            high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            same_detail = "您的GPA选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (gpa < gpa_score) {
                        high_detail = "您的SAT,GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (sat > sat_score) {
                            low_detail = "您在SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat == sat_score) {
                            same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                            high_detail = "您的GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,SAT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (sat < sat_score) {
                            high_detail = "您的SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA,SAT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                }
            }
            // AP ACT
            if (scoreAct != null && !scoreAct.equals("")) {
                if (ap > ap_score) {
                    low_detail = "您在AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                    if (act > act_score) {
                        low_detail = "您在ACT成绩,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (gpa < gpa_score) {
                            high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa == gpa_score){
                            same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT,GPR,AP与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act == act_score) {
                        low_detail = "您在AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        same_detail = "您的ACT成绩与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act < act_score) {
                        low_detail = "您在AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        high_detail = "您的ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                }else if(ap == ap_score){
                    same_detail = "您的AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                    if (act > act_score) {
                        low_detail = "您在ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (gpa < gpa_score) {
                            high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa == gpa_score){
                            same_detail = "您的GPR,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT,GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act == act_score) {
                        same_detail = "您的ACT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act < act_score) {
                        high_detail = "您的ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                }else if(ap < ap_score){
                    high_detail = "您的AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                    if (act > act_score) {
                        low_detail = "您在ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (gpa < gpa_score) {
                            high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa == gpa_score){
                            same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT,GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act == act_score) {
                        same_detail = "您的ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    } else if (act < act_score) {
                        high_detail = "您的ACT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (gpa == gpa_score) {
                            same_detail = "您的GPA,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if(gpa > gpa_score){
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }else if (gpa < gpa_score) {
                            high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,GPA,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        }
                    }
                }
            }

        }
        if(scoreIb != null && !scoreIb.equals("")){
            //IB SAT
            if(scoreSat != null && !scoreSat.equals("")){
                if (scoreSat != null && !scoreSat.equals("")) {
                    if (ib > ib_score) {
                        low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (gpa > gpa_score) {
                            low_detail = "您在GPA,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (sat > sat_score) {
                                low_detail = "您在GPA,SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,SAT,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                                low_detail = "您在GPA,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                low_detail = "您在GPA,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa == gpa_score) {
                            if (sat > sat_score) {
                                low_detail = "您在SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,SAT,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                                low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa < gpa_score) {
                            if (sat > sat_score) {
                                low_detail = "您在SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩高\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,SAT,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                                low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩高\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    } else if (ib == ib_score) {
                        same_detail = "您在IB上和" + schoolName + "中国留学生的平均成绩一致。";
                        if (gpa > gpa_score) {
                            low_detail = "您在GPA,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (sat > sat_score) {
                                low_detail = "您在GPA,SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT，IB上和" + schoolName + "中国留学生的平均成绩一致。";
                                low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT，IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                low_detail = "您在GPA,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa < gpa_score) {
                            high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            low_detail = "您在IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (sat > sat_score) {
                                low_detail = "您在GPA,SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA,SAT,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT,IB上和" + schoolName + "中国留学生的平均成绩一致。";
                                high_detail = "您的GPA方面比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                same_detail = "您的IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                high_detail = "您的GPA,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa == gpa_score) {
                            if (sat > sat_score) {
                                low_detail = "您在SAT,IB方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                same_detail = "您的GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,SAT与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT,IB,GPA选课上和" + schoolName + "中国留学生的平均成绩一致。";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA,SAT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                same_detail = "您的GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                high_detail = "您的SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    } else if (ib < ib_score) {
                        if (gpa > gpa_score) {
                            low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (sat > sat_score) {
                                low_detail = "您在GPA,SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                                low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT，IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名，IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                low_detail = "您在GPA方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa == gpa_score) {
                            same_detail = "您的GPA选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (sat > sat_score) {
                                low_detail = "您在SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT,GPA上和" + schoolName + "中国留学生的平均成绩一致。";
                                high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT,GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                same_detail = "您的GPA选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (gpa < gpa_score) {
                            high_detail = "您的SAT,GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (sat > sat_score) {
                                low_detail = "您在SAT方面与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA,SAT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat == sat_score) {
                                same_detail = "您在SAT上和" + schoolName + "中国留学生的平均成绩一致。";
                                high_detail = "您的GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,SAT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (sat < sat_score) {
                                high_detail = "您的SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA,SAT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    }
                }
            }

            //IB ACT
            if (scoreAct != null && !scoreAct.equals("")){
                if (scoreAct != null && !scoreAct.equals("")) {
                    if (ib > ib_score) {
                        low_detail = "您在IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                        if (act > act_score) {
                            low_detail = "您在ACT成绩,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (gpa < gpa_score) {
                                high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa == gpa_score){
                                same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT,GPR,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act == act_score) {
                            low_detail = "您在IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            same_detail = "您的ACT成绩与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act < act_score) {
                            low_detail = "您在IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            high_detail = "您的ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    }else if(ib == ib_score){
                        same_detail = "您的IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                        if (act > act_score) {
                            low_detail = "您在ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (gpa < gpa_score) {
                                high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa == gpa_score){
                                same_detail = "您的GPR,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT,GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act == act_score) {
                            same_detail = "您的ACT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act < act_score) {
                            high_detail = "您的ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    }else if(ib < ib_score){
                        high_detail = "您的IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                        if (act > act_score) {
                            low_detail = "您在ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力！\n";
                            if (gpa < gpa_score) {
                                high_detail = "您的GPR比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa == gpa_score){
                                same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT,GPR与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act == act_score) {
                            same_detail = "您的ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        } else if (act < act_score) {
                            high_detail = "您的ACT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            if (gpa == gpa_score) {
                                same_detail = "您的GPA,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if(gpa > gpa_score){
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }else if (gpa < gpa_score) {
                                high_detail = "您的GPA,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,GPA,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            }
                        }
                    }
                }
            }
        }
        return tf_detail + low_detail + high_detail + same_detail;
    }
}

