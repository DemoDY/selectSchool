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
        int ielts = 0;
        int toefl = 0;
        String act_sat = "";
        String ib_ap = "";
        String sex = "";
        String sat = "";
        String ib = "";
        String gpa = "";
        String act = "";
        String rank = "";
        String tf = "";
        String ys = "";
        String ap = "";
        String one = "";
        String two = "";
        String three = "";
        int ninthing = 0;
        int twenty = 0;
        String nosat = null;
        UserScores userScores = new UserScores();//学生选择问题 总数表
        logger.info("\n optionList:" + options.size());
        Scores scores = new Scores();//学校选择问题报错表
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
                    act = option.getNumber();
                }
                //15 题
                if (problemId.equals("17")) {
                    ib_ap = "AP";
                    ap = option.getNumber();
                } else if (problemId.equals("18")) {
                    ib_ap = "IB";
                    ib = option.getNumber();
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
                if (seq == 4) {
                    ib_ap = "AP";
                    ap = "0";
                }
            }

            if (problemId.equals("13")) {
                int seq = option.getSeq();
                //如果第13题选择了第三个选择 那么 SAT成绩为1200 平均成绩为90
                if (seq == 3) {
                    act_sat = "SAT";
                    sat = "90";
                    nosat = "0";
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
                    ielts = option.getSeq();
                    ys = option.getOption();
                    logger.info("雅思成绩:" + ys);
                }
                //托福 TOEFL
                if (tl.equals("TOEFL")) {
                    toefl = option.getSeq();
                    tf = option.getOption();
                    logger.info("托福成绩:" + tf);
                }
            }
        }
        if (toefl != 0 && ielts != 0) {
            if (ielts == 7 || ielts == 1) {
                ys = "7.5";
            }
            if (ielts == 2) ys = "7";
            if (ielts == 3) ys = "6.5";
            if (ielts == 4) ys = "6";
            if (ielts == 5) ys = "5.5";
            if (ielts == 6) ys = "5";
            if (ielts == 8) {
                ys = "0";
            }
            // TODO 判断的数据都是绝对数值  禁止数据库有其他字 或者符号空格一类
            if (toefl == 1 || toefl == 7) {
                // 1 如果 托福 是大于100分 以托福为主。不管雅思多少分
                if (ielts == 1 || ielts == 2 || ielts == 3 || ielts == 4 || ielts == 5 || ielts == 6 || ielts == 7 || ielts == 8) {
                    userScores.setTl(2); //雅思是1  托福 是2
                    userScores.setTlScore(tf);
                    tf = "100";
                    ys = "0";
                }
            }
            if (toefl == 2) {
                // 2 如果 托福 是99-90分 而 雅思是 7.5以上  以雅思为主 否则以托福为主。
                if (ielts == 1 || ielts == 7) {
                    tf = "0";
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                }
                if (ielts == 2 || ielts == 3 || ielts == 4 || ielts == 5 || ielts == 6 || ielts == 8) {
                    userScores.setTl(2);
                    userScores.setTlScore(tf);
                    tf = "90";
                    ys = "0";
                }
            }
            if (toefl == 3) {
                // 3 如果 托福 是89----79分 而雅思是 7以上 以雅思为主 否则以托福为主。
                if (ielts == 1 || ielts == 2 || ielts == 7) {
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                    tf = "0";
                }
                if (ielts == 3 || ielts == 4 || ielts == 5 || ielts == 6 || ielts == 8) {
                    userScores.setTl(2);
                    userScores.setTlScore(tf);
                    tf = "79";
                    ys = "0";
                }
            }
            if (toefl == 4) {
                // 4 如果 托福 是78----69分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts == 1 || ielts == 2 || ielts == 7 || ielts == 3) {
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                    tf = "0";
                }
                if (ielts == 4 || ielts == 5 || ielts == 6 || ielts == 8) {
                    userScores.setTlScore(tf);
                    userScores.setTl(2);
                    tf = "69";
                    ys = "0";
                }
            }
            if (toefl == 5) {
                // 5 如果 托福 是68----61分 而雅思是 6.5以上 以雅思为主 否则以托福为主。
                if (ielts == 1 || ielts == 2 || ielts == 7 || ielts == 3 || ielts == 4) {
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                    tf = "0";
                }
                if (ielts == 5 || ielts == 6 || ielts == 8) {
                    userScores.setTlScore(tf);
                    userScores.setTl(2);
                    tf = "61";
                    ys = "0";
                }
            }
            if (toefl == 6) {
                if (ielts == 1 || ielts == 2 || ielts == 7 || ielts == 3 || ielts == 4 || ielts == 5) {
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                    tf = "0";
                }
                if (ielts == 6 || ielts == 8) {
                    userScores.setTlScore(tf);
                    userScores.setTl(2);
                    tf = "60";
                    ys = "0";
                }
            }
            if (toefl == 8) {
                if (ielts == 1 || ielts == 2 || ielts == 7 || ielts == 3 || ielts == 4 || ielts == 5 || ielts == 6) {
                    userScores.setTl(1);
                    userScores.setTlScore(ys);
                    tf = "0";
                }
                if (ielts == 8) {
                    userScores.setTlScore("0");
                    userScores.setTl(2);
                    tf = "0";
                    ys = "0";
                }
            }
        }
        if (sex.equals("女")) {
            //如果女生ap总成绩大于等于490 加10分
            if (ib_ap.equals("AP") && num >= 490) {
                num = num + 10;
            }
            //如果女生IB总成绩大于等于480 加10分
            if (ib_ap.equals("IB") && num >= 480) {
                num = num + 10;
            }
        }
        // 第六题 如果选择1 加20
        if (StringUtils.isNotBlank(one)) {
            num = num + 20;
            userScores.setScores(num);
            userScores.setSixQuestion(one);
            //如果选择1和2 加25
        } else if (StringUtils.isNotBlank(one) && StringUtils.isNotBlank(two)) {
            num = num + 25;
            userScores.setScores(num);
            userScores.setSixQuestion("4");
            //如果选择2 加10
        } else if (StringUtils.isBlank(one) && StringUtils.isNotBlank(two)) {
            num = num + 10;
            userScores.setScores(num);
            userScores.setSixQuestion(two);
            //如果两个都没选择
        } else if (StringUtils.isBlank(one) && StringUtils.isBlank(two) && StringUtils.isNotBlank(three)) {
            userScores.setScores(num);
            userScores.setSixQuestion("美国大学招生时比对您所在学校以往的录取记录，由于您的高中上两届毕业生中没有顶尖名校的录取记录，您得加倍努力。");
        }
        if (tf.equals("0") && ys.equals("0")) {
            return AjaxResult.error(400, "因为英语成绩太低，在全美前300名的学校中无匹配梦想，目标和保底学校。");
        }
        if (toefl == 8 && ielts == 6) {
            return AjaxResult.error(400, "因为英语成绩太低，在全美前300名的学校中无匹配梦想，目标和保底学校。");
        }
        if (toefl == 6 && ielts == 8) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        if (toefl == 6 && ielts == 6) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        //nosat不为null则没有选择sat成绩
        if (nosat != null && ib_ap.equals("IB") && num < 85) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        if (nosat != null && ib_ap.equals("AP") && num < 65) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        //nosat为null则选择了sat成绩
        if (nosat == null && ib_ap.equals("IB") && num < 5) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        if (nosat == null && ib_ap.equals("AP") && num < 15) {
            return AjaxResult.error(400, "在全美前300的综合性大学中，没有学校匹配您的输入条件。");
        }
        userScores.setCreateTime(new Date());
        userScores.setActSat(act_sat);
        userScores.setApIb(ib_ap);
        userScores.setNoSat(nosat);
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
        scores.setIeltsLow(ys);
        scoresMapper.insertScores(scores);
        ajaxResult.put("userScores", userScores1);
//        return AjaxResult.successData(200, ajaxResult);
        return AjaxResult.error(400, "此功能未开通，请稍后再试");
    }

    /**
     * 根据学生成绩查询 九所学校
     *
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
        if (!q.equals("2") && !q.equals("1")) {
            question = userScores.getSixQuestion();
        }
        String noSat = userScores.getNoSat();//不需要sat的字段
        int scores = userScores.getScores();
        String tlScore = userScores.getTlScore();//托福成绩
        ReportFileDTO reportFileDTOS = new ReportFileDTO();
        SchoolProfileVo schoolProfileVo = new SchoolProfileVo();//学校详情
        List<SchoolAdmissionScores> schoolAdmissionScores = null;
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
        reportFileDTOS.setQuestion("\t你的学校的录取概率是x%, 简单地说就是100个和你同样水平的中国学生申请，只有x个学生会被录取。" + "\n\t" + question);
        reportFileDTOS.setExplain("\n若有兴趣了解更多，可联系我们的客服 >>>>>>>>>>>>客服微信号\n");
//        reportFileDTOS.setKefuweChat("http://www.desmart.com.cn/schoolLogo/ererima.png");//微信二维码
        double score = 0;
       /* if (tlScore.equals(">=100") || tlScore.equals("免考")) score = 100;
        if (tlScore.equals("99----90")) score = 99;
        if (tlScore.equals("89----79")) score = 89;*/
        if (tl == 2) {//托福成绩
            if (scores >= 500) {
                score = 100;
            } else if (scores >= 400) {
                score = 99;
            } else {
                if (tlScore.equals("89----79")) score = 89;
                if (tlScore.equals("78----69")) score = 78;
                if (tlScore.equals("68----61")) score = 68;
                if (tlScore.equals("小于61")) score = 60;
            }
        }
        if (tl == 1) {//雅思成绩
            /*if (tlScore.equals("7.5") || tlScore.equals("免考")) score = 7.5;
            if (tlScore.equals("7")) score = 7;
            if (tlScore.equals("6.5")) score = 6.5;*/
            if (scores >= 500) {
                score = 7.5;
            } else if (scores >= 400) {
                score = 7;
            } else {
                if (tlScore.equals("6.5")) score = 6.5;
                if (tlScore.equals("6")) score = 6;
                if (tlScore.equals("5.5")) score = 5.5;
                if (tlScore.equals("低于5.5")) score = 5.4;
            }
        }
        if (sat_act.equals("ACT")) {
            if (ib_ap.equals("IB")) {
                //梦想学校 Dream School
                if (noSat != null && scores > 194) {
                    // 如果没有选择sat成绩 并且粉数大于194 则选择固定的学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(76);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(84);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(86);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else if (scores >= 620 && noSat == null) {
                    //如果ib分数大于710 则选择前三名学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(1);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(2);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(3);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() + 20)
                            .addQuery("ibActWeightMax", userScores.getScores() + 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0)
                            .addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        dreamSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }

                //目标学校 Target School
                if (scores >= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 240)
                            .addQuery("ibActWeightMax", userScores.getScores() - 130).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 580 && scores <= 649 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 200)
                            .addQuery("ibActWeightMax", userScores.getScores() - 100).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores >= 300) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(121);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(122);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(125);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    targetSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", userScores.getScores() - 50)
                            .addQuery("ibActWeightMax", userScores.getScores() - 5).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //保底学校 Safety colleges
                //因为ib成绩和ap成绩不一样  说一区间分数也不同（ib成绩比ap成绩高些）
                if (scores >= 500 && scores <= 600 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                            .addQuery("ibActWeightMax", userScores.getScores() - 150).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 600 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                            .addQuery("ibActWeightMax", userScores.getScores() - 240).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 700 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                            .addQuery("ibActWeightMax", userScores.getScores() - 260).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores > 270) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(139);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(142);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(149);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    safetyColleges(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibActWeightMin", 0)
                            .addQuery("ibActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
            }
            if (ib_ap.equals("AP")) {//act ap
                //梦想学校
                if (noSat != null && scores > 194) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(76);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(84);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(86);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else if (scores >= 620 && noSat == null) {
                    //如果ap成绩大于620 则选择前三名学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(1);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(2);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(3);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() + 20)
                            .addQuery("apActWeightMax", userScores.getScores() + 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0)
                            .addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //梦想学校
                        dreamSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //目标学校 Target School
                if (scores >= 660 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 220)
                            .addQuery("apActWeightMax", userScores.getScores() - 120).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 580 && scores <= 659 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 200)
                            .addQuery("apActWeightMax", userScores.getScores() - 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores >= 280) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(91);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(92);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(95);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    targetSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", userScores.getScores() - 50)
                            .addQuery("apActWeightMax", userScores.getScores() - 5).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //保底学校 Safety colleges
                //因为ib成绩和ap成绩不一样  说一区间分数也不同（ib成绩比ap成绩高些）
                if (scores >= 500 && scores <= 600 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                            .addQuery("apActWeightMax", userScores.getScores() - 120).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 600 && scores <= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                            .addQuery("apActWeightMax", userScores.getScores() - 160).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                            .addQuery("apActWeightMax", userScores.getScores() - 200).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores > 270) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(106);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(114);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(115);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    safetyColleges(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apActWeightMin", 0)
                            .addQuery("apActWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
            }
        }
        if (sat_act.equals("SAT")) {
            if (ib_ap.equals("IB")) {//sat ib
                //梦想学校
                if (noSat != null && scores > 194) {
                    // 如果没有选择sat成绩 并且粉数大于194 则选择固定的学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(76);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(84);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(86);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else if (scores >= 620 && noSat == null) {
                    //如果ib成绩大于710 则选择前三名学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(1);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(2);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(3);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() + 20)
                            .addQuery("ibSatWeightMax", userScores.getScores() + 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0)
                            .addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //梦想学校
                        dreamSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //目标学校 Target School
                if (scores >= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 210)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 130).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 580 && scores <= 649 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 150)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores >= 300) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(121);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(122);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(125);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    targetSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);

                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", userScores.getScores() - 50)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 5).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //保底学校 Safety colleges
                if (scores >= 500 && scores <= 600 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 150).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 600 && scores <= 700 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 200).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 700 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 250).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores > 270) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(139);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(142);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(149);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    safetyColleges(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("ibSatWeightMin", 0)
                            .addQuery("ibSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
            }
            if (ib_ap.equals("AP")) {//sat ap
                //梦想学校 求具体分数 得出三个学校
                if (noSat != null && scores > 194) {
                    // 如果没有选择sat成绩 并且粉数大于194 则选择固定的学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(76);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(84);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(86);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else if (scores >= 620 && noSat == null) {
                    //如果ap成绩大于620 则选择前三名学校
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(1);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(2);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(3);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    dreamSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() + 20)
                            .addQuery("apSatWeightMax", userScores.getScores() + 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0)
                            .addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //梦想学校
                        dreamSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //目标学校 Target School
                if (scores >= 660 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 220)
                            .addQuery("apSatWeightMax", userScores.getScores() - 130).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 580 && scores <= 559 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 160)
                            .addQuery("apSatWeightMax", userScores.getScores() - 80).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores >= 280) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(91);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(92);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(95);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    targetSchool(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", userScores.getScores() - 60)
                            .addQuery("apSatWeightMax", userScores.getScores() - 5).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        //目标学校
                        targetSchool(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
                //保底学校 Safety colleges
                if (scores >= 500 && scores <= 600 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                            .addQuery("apSatWeightMax", userScores.getScores() - 120).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 600 && scores <= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                            .addQuery("apSatWeightMax", userScores.getScores() - 160).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (scores >= 650 && noSat == null) {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                            .addQuery("apSatWeightMax", userScores.getScores() - 200).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                } else if (noSat != null && scores >= 270) {
                    List<SchoolAdmissionScores> scoresList = new ArrayList<>();
                    SchoolAdmissionScores s = schoolAdmissionScoresMapper.selectId(106);
                    SchoolAdmissionScores a = schoolAdmissionScoresMapper.selectId(114);
                    SchoolAdmissionScores c = schoolAdmissionScoresMapper.selectId(115);
                    if (s != null) scoresList.add(s);
                    if (a != null) scoresList.add(a);
                    if (c != null) scoresList.add(c);
                    safetyColleges(scoresList, reportFileDTOS, userScores, schoolProfileVo);
                } else {
                    schoolAdmissionScores = schoolAdmissionScoresMapper.selectIbActWeightDream(sql.addQuery("apSatWeightMin", 0)
                            .addQuery("apSatWeightMax", userScores.getScores() - 50).addQuery("toeflHigh", score).addQuery("toeflLow", 0).addQuery("tl", tl).addQuery("noSat", noSat).getMap());
                    if (schoolAdmissionScores.size() > 0) {//如果大于0
                        if (schoolAdmissionScores.size() >= 3) {
                            //如果大于3 需要截取前三所学校
                            schoolAdmissionScores = schoolAdmissionScores.subList(0, 3);
                        }
                        safetyColleges(schoolAdmissionScores, reportFileDTOS, userScores, schoolProfileVo);
                    }
                }
            }
        }
        ajaxResult.put("school", reportFileDTOS);
        return ajaxResult;
    }

    //保底学校
    private void safetyColleges(List<SchoolAdmissionScores> schoolAdmissionScores, ReportFileDTO
            reportFileDTO, UserScores userScores, SchoolProfileVo schoolVo) {
        List<SafetySchoolDTO> safetySchoolDTOS = new ArrayList<>();
        for (SchoolAdmissionScores school : schoolAdmissionScores) {
            SchoolAdmissionScores scores = schoolAdmissionScoresMapper.selectId(school.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(scores.getSchoolId());
            SafetySchoolDTO safetySchoolDTO = new SafetySchoolDTO();
            //雷达图数据 radarMapVo
            List<RadarMapVo> radarMapVo = radarMap(scores, userScores);
            List<SchoolDetails> schoolDetailsList = schoolDetails(scores,schoolProfile);
            //学校详情数据
            String detail = selectDetailsSafety(scores, userScores, schoolProfile);
            safetySchoolDTO.setDetails(detail);
            safetySchoolDTO.setChName(schoolProfile.getChName());//学校中文名
            safetySchoolDTO.setSchoolName(schoolProfile.getSchoolName());//学校英文名
            safetySchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());//学校简介
            safetySchoolDTO.setCrest(schoolProfile.getCrest());
            safetySchoolDTO.setSafetySchool("Safety School 保底学校：\n");
            safetySchoolDTO.setRadarMap(radarMapVo);//保存类雷达图
            safetySchoolDTO.setSchoolDetails(schoolDetailsList);//学习详情数据
            safetySchoolDTOS.add(safetySchoolDTO);  // 保底学校数据
        }
        schoolVo.setSafetySchoolDTOS(safetySchoolDTOS);//把保底学校的数据放入list 里面
        reportFileDTO.setSchoolProfileVos(schoolVo);
    }

    //目标学校
    private void targetSchool(List<SchoolAdmissionScores> schoolAdmissionScores, ReportFileDTO
            reportFileDTO, UserScores userScores, SchoolProfileVo schoolProfileVo) {
        List<TargetSchoolDTO> targetSchoolDTOS = new ArrayList<>();
        for (SchoolAdmissionScores school : schoolAdmissionScores) {
            SchoolAdmissionScores scores = schoolAdmissionScoresMapper.selectId(school.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(scores.getSchoolId());
            TargetSchoolDTO targetSchoolDTO = new TargetSchoolDTO();
            //雷达图数据 radarMapVo
            List<RadarMapVo> radarMapVo = radarMap(scores, userScores);
            List<SchoolDetails> schoolDetailsList = schoolDetails(scores,schoolProfile);
            //学校详情数据
            targetSchoolDTO.setChName(schoolProfile.getChName());
            targetSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            String detail = selectDetailsSafety(scores, userScores, schoolProfile);
            targetSchoolDTO.setDetails(detail);
            targetSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            targetSchoolDTO.setCrest(schoolProfile.getCrest());
            targetSchoolDTO.setTargetSchool("Target School 目标学校：\n");
            targetSchoolDTO.setRadarMap(radarMapVo);
            targetSchoolDTO.setSchoolDetails(schoolDetailsList);//学习详情数据
            targetSchoolDTOS.add(targetSchoolDTO);
        }
        schoolProfileVo.setTargetSchoolDTOS(targetSchoolDTOS);
        reportFileDTO.setSchoolProfileVos(schoolProfileVo);
    }

    //梦想学校
    private void dreamSchool(List<SchoolAdmissionScores> schoolAdmissionScores, ReportFileDTO
            reportFileDTO, UserScores userScores, SchoolProfileVo schoolVo) {
        List<DreamSchoolDTO> dreamSchoolDTOS = new ArrayList<>();
        for (SchoolAdmissionScores school : schoolAdmissionScores) {
            SchoolAdmissionScores scores = schoolAdmissionScoresMapper.selectId(school.getId());
            SchoolProfile schoolProfile = schoolProfileMapper.selectById(scores.getSchoolId());
            DreamSchoolDTO dreamSchoolDTO = new DreamSchoolDTO();
            //雷达图数据 radarMapVo
            List<RadarMapVo> radarMapVo = radarMap(scores, userScores);
            List<SchoolDetails> schoolDetailsList = schoolDetails(scores,schoolProfile);
            //学校详情数据
            dreamSchoolDTO.setChName(schoolProfile.getChName());
            dreamSchoolDTO.setSchoolName(schoolProfile.getSchoolName());
            dreamSchoolDTO.setSchoolProfile("\t" + schoolProfile.getSchoolProfile());
            String detail = selectDetailsSafety(scores, userScores, schoolProfile);
            dreamSchoolDTO.setDetails(detail);
            dreamSchoolDTO.setCrest(schoolProfile.getCrest());
            dreamSchoolDTO.setDreamSchool("Dream School 梦想学校：\n");
            dreamSchoolDTO.setSchoolDetails(schoolDetailsList);//学习详情数据
            dreamSchoolDTO.setRadarMap(radarMapVo);//学习雷达图数据
            dreamSchoolDTOS.add(dreamSchoolDTO);//梦想学校
        }
        schoolVo.setDreamSchoolDTOS(dreamSchoolDTOS);
        reportFileDTO.setSchoolProfileVos(schoolVo);//保存学校详情
    }

    //获取学校详情数据信息
    private List<SchoolDetails> schoolDetails(SchoolAdmissionScores admissionScores,SchoolProfile schoolProfile) {
        List<SchoolDetails> schoolDetailsList = new ArrayList<>();

        SchoolDetails schoolDetailsA = new SchoolDetails();
        int nineteenInt = admissionScores.getNineteen();
        String nineteen = String.valueOf(nineteenInt);
        schoolDetailsA.setDetails(nineteen);
        schoolDetailsA.setName("US NEW 2019排名");
        schoolDetailsList.add(schoolDetailsA);

        SchoolDetails schoolDetailsB = new SchoolDetails();
        String twenty = String.valueOf(admissionScores.getTwenty());
        schoolDetailsB.setDetails(twenty);
        schoolDetailsB.setName("US NEW 2020排名");
        schoolDetailsList.add(schoolDetailsB);

        SchoolDetails schoolDetailsC = new SchoolDetails();
        String tuitionfees = admissionScores.getTuitionFees();
        if (tuitionfees != null && !tuitionfees.equals("")) {
            schoolDetailsC.setDetails(tuitionfees);
            schoolDetailsC.setName("学费（美元）");
            schoolDetailsList.add(schoolDetailsC);
        }

        SchoolDetails schoolDetailsD = new SchoolDetails();
        String schoolName = schoolProfile.getChName();
        if (schoolName != null && !schoolName.equals("")) {
            schoolDetailsD.setDetails(schoolName);
            schoolDetailsD.setName("大学名称");
            schoolDetailsList.add(schoolDetailsD);
        }

        SchoolDetails schoolDetailsE = new SchoolDetails();
        String numNationalFreshmen = admissionScores.getNumNationalFreshmen();
        if (numNationalFreshmen != null && !numNationalFreshmen.equals("")) {
            schoolDetailsE.setDetails(numNationalFreshmen);
            schoolDetailsE.setName("国际生大一人数");
            schoolDetailsList.add(schoolDetailsE);
        }

        SchoolDetails schoolDetailsF = new SchoolDetails();
        String nationalStuAccep = admissionScores.getNationalStuAccep();
        if (nationalStuAccep != null && !nationalStuAccep.equals("")){
            schoolDetailsF.setDetails(nationalStuAccep);
            schoolDetailsF.setName("国际生录取率");
            schoolDetailsList.add(schoolDetailsF);
         }

        SchoolDetails schoolDetailsG = new SchoolDetails();
        int firstStudentsInt = admissionScores.getFirstStudentsNum();
        String firstStudentsString = String.valueOf(firstStudentsInt);
        schoolDetailsG.setDetails(firstStudentsString);
        schoolDetailsG.setName("留学生大一入学人数");
        schoolDetailsList.add(schoolDetailsG);

        return schoolDetailsList;
    }

    /**
     * 获取雷达图数据
     *
     * @param admissionScores
     * @return
     */
    private List<RadarMapVo> radarMap(SchoolAdmissionScores admissionScores, UserScores userScores) {
        List<RadarMapVo> radarMapVoList = new ArrayList<>();
        Scores scores = scoresMapper.findByUserScoreId(userScores.getId());
        //雷达图数据 radarMapVo
        RadarMapVo radarMapVoAct = new RadarMapVo();
        int act = admissionScores.getActWeight();
        String mact = scores.getActAvg();
        if (mact != null && !mact.equals("")) {
            int iact = Integer.parseInt(mact);
            String percentInteract = DateUtil.getPercentInter(act, 200);
            String Interact = DateUtil.getPercentInter(iact, 200);
            radarMapVoAct.setName("ACT");
            radarMapVoAct.setSelfMap(Interact);
            radarMapVoAct.setSchoolMap(percentInteract);
            radarMapVoList.add(radarMapVoAct);
        }

        RadarMapVo radarMapVoAp = new RadarMapVo();
        int ap = admissionScores.getApWeight();
        String map = scores.getApCourse();
        String num = String.valueOf(ap);
        if (map != null && !map.equals("")) {
            int iap = Integer.parseInt(map);
            String percentInterap = DateUtil.getPercentInter(ap, 80);
            String Interap = DateUtil.getPercentInter(iap, 80);
            radarMapVoAp.setName("AP");
            radarMapVoAp.setSelfMap(Interap);
            radarMapVoAp.setSchoolMap(percentInterap);
            radarMapVoList.add(radarMapVoAp);
        }

        RadarMapVo radarMapVoGpa = new RadarMapVo();
        String gpa = admissionScores.getChGpaWeightStu();
        String mgpa = scores.getGpaAvg();
        if (!mgpa.equals("") && mgpa != null) {
            int i = Integer.parseInt(gpa);//把录取率转成int类型 用来计算
            int igpa = Integer.parseInt(mgpa);//把录取率转成int类型 用来计算
            String percentInter = DateUtil.getPercentInter(i, 200);
            String percentIntergpa = DateUtil.getPercentInter(igpa, 200);
            radarMapVoGpa.setSchoolMap(percentInter);
            radarMapVoGpa.setSelfMap(percentIntergpa);
            radarMapVoGpa.setName("GPA");
            radarMapVoList.add(radarMapVoGpa);
        }


        RadarMapVo radarMapVoIb = new RadarMapVo();
        int ib = admissionScores.getIbWeight();
        String mib = scores.getIbAvg();
        if (mib != null && !mib.equals("")) {
            String percentInterib = DateUtil.getPercentInter(ib, 170);
            int iIb = Integer.parseInt(mib);
            String interIb = DateUtil.getPercentInter(iIb, 170);
            radarMapVoIb.setSchoolMap(percentInterib);
            radarMapVoIb.setSelfMap(interIb);
            radarMapVoIb.setName("IB");
            radarMapVoList.add(radarMapVoIb);
        }

        RadarMapVo radarMapVoRank = new RadarMapVo();
        int rank = admissionScores.getNineteen();//排名
        String mRank = scores.getRank();
        int iRank = Integer.parseInt(mRank);
        if (mRank != null && !mRank.equals("")) {
//            String percentInter = DateUtil.getPercentInter(rank, 300);
            String percentRank = DateUtil.getPercentInter(iRank, 100);
            radarMapVoRank.setSchoolMap("60");
            radarMapVoRank.setSelfMap(percentRank);
            radarMapVoRank.setName("排名");
            radarMapVoList.add(radarMapVoRank);
        }

        RadarMapVo radarMapVoys = new RadarMapVo();
        double ielts = admissionScores.getIeltsLowReq();
        String mIelts = scores.getIeltsLow();
        double dIelts = Double.parseDouble(mIelts);
        if (!mIelts.equals("0")) {
            String percent = DateUtil.getPercentInter(ielts, 7.5);
            String percentIelts = DateUtil.getPercentInter(dIelts, 7.5);
            radarMapVoys.setSchoolMap(percent);
            radarMapVoys.setSelfMap(percentIelts);
            radarMapVoys.setName("IELTS");
            radarMapVoList.add(radarMapVoys);
        }

        RadarMapVo radarMapVotolfl = new RadarMapVo();
        String mToefl = scores.getToeflLow();
        int mt = Integer.parseInt(mToefl);
        int tolfl = admissionScores.getToeflLowReq();//托福成绩
        if (!mToefl.equals("0")) {
            String percentInter = DateUtil.getPercentInter(tolfl, 100);
            String percentIntermt = DateUtil.getPercentInter(mt, 100);
            radarMapVotolfl.setSchoolMap(percentInter);
            radarMapVotolfl.setSelfMap(percentIntermt);
            radarMapVotolfl.setName("TOLFL");
            radarMapVoList.add(radarMapVotolfl);
        }

        RadarMapVo radarMapVoSat = new RadarMapVo();
        int sat = admissionScores.getSatWeight();//sat成绩
        String mSat = scores.getSatAvgHigh();
        if (mSat != null && !mSat.equals("")) {
            int iSat = Integer.parseInt(mSat);
            String percentIntersat = DateUtil.getPercentInter(sat, 200);
            String interSat = DateUtil.getPercentInter(iSat, 200);
            radarMapVoSat.setSchoolMap(percentIntersat);
            radarMapVoSat.setSelfMap(interSat);
            radarMapVoSat.setName("SAT");
            radarMapVoList.add(radarMapVoSat);
        }
        return radarMapVoList;
    }


    /**
     * 判断学校和自己分数的差距
     *
     * @param admissionScores
     * @param userScores
     * @param schoolProfile
     * @return
     */
    private String selectDetailsSafety(SchoolAdmissionScores admissionScores, UserScores userScores, SchoolProfile
            schoolProfile) {
        String schoolName = schoolProfile.getChName();//学校名称
        int userid = userScores.getId();
        Scores scores = scoresMapper.findByUserScoreId(userid);
        int ap = admissionScores.getApWeight();//ap 学校选课权重
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
        int act = admissionScores.getActAvgResults();
        String scoreAct = scores.getActAvg();
        int act_score = 0;
        if (scoreAct != null && !scoreAct.equals("")) {
            act_score = Integer.parseInt(scoreAct);
        }
        int ib = admissionScores.getIbAvgResults();
        int ib_score = 0;
        String scoreIb = scores.getIbAvg();
        if (!scoreIb.equals("") && scoreIb != null) {
            ib_score = Integer.parseInt(scoreIb);
        }
        int sat = admissionScores.getSatWeight();
        String scoreSat = scores.getSatAvgHigh();
        int sat_score = 0;
        if (scoreSat != null && !scoreSat.equals("")) {
            sat_score = Integer.parseInt(scoreSat);
        }
        int rank = Integer.parseInt(admissionScores.getChStuWeightRank());//中国学生排名权重
        int rank_score = Integer.parseInt(scores.getRank());
        int toefl_score = userScores.getTl();//托福 选择的成绩

        String mIelts = scores.getIeltsLow();
        double dIelts = Double.parseDouble(mIelts);
        double schoolYs = admissionScores.getIeltsLowReq();

        String mToefl = scores.getToeflLow();
        int mt = Integer.parseInt(mToefl);
        int schoolTf = admissionScores.getToeflLowReq();
        String tf_detail = "";
        if (toefl_score == 2) {
            if (mt >= schoolTf) {
                tf_detail = "您的TOEFL成绩符合" + schoolName + "录取要求。\n";
            } else {
                tf_detail = "您的TOEFL成绩不符合" + schoolName + "录取要求。\n";
            }
        }
        if (toefl_score == 1) {
            if (dIelts >= schoolYs) {
                tf_detail = "您的IELTS成绩符合" + schoolName + "录取要求。\n";
            } else {
                tf_detail = "您的IELTS成绩不符合" + schoolName + "录取要求。\n";
            }
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
                        } else if (gpa == gpa_score) {
                            same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩,AP选课与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa > gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,AP选课,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
                } else if (ap == ap_score) {
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
                        } else if (gpa == gpa_score) {
                            same_detail = "您的GPR,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa > gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,AP选课与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
                } else if (ap < ap_score) {
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
                        } else if (gpa == gpa_score) {
                            same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa > gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
                        } else if (gpa > gpa_score) {
                            low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            if (rank > rank_score) {
                                low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                            } else if (rank == rank_score) {
                                same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                            } else if (rank < rank_score) {
                                high_detail = "您的年级排名,ACT,AP选课比历届" + schoolName + "中国留学生的平均成绩高。\n";
                            }
                        } else if (gpa < gpa_score) {
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
        if (scoreIb != null && !scoreIb.equals("")) {
            //IB SAT
            if (scoreSat != null && !scoreSat.equals("")) {
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
            if (scoreAct != null && !scoreAct.equals("")) {
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
                            } else if (gpa == gpa_score) {
                                same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩,IB与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa > gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,IB,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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
                    } else if (ib == ib_score) {
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
                            } else if (gpa == gpa_score) {
                                same_detail = "您的GPR,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa > gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,IB与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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
                    } else if (ib < ib_score) {
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
                            } else if (gpa == gpa_score) {
                                same_detail = "您的GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,ACT成绩与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,GPR与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa > gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名,ACT与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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
                            } else if (gpa > gpa_score) {
                                low_detail = "您的GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                if (rank > rank_score) {
                                    low_detail = "您的年级排名,GPA与历届" + schoolName + "中国留学生的平均成绩有差距，还需努力。\n";
                                } else if (rank == rank_score) {
                                    same_detail = "您的年级排名与历届" + schoolName + "中国留学生的平均成绩一致。\n";
                                } else if (rank < rank_score) {
                                    high_detail = "您的年级排名,ACT,IB比历届" + schoolName + "中国留学生的平均成绩高。\n";
                                }
                            } else if (gpa < gpa_score) {
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

