package com.select.school.service.web.impl;

import com.select.school.mapper.SchoolAdmissionScoresMapper;
import com.select.school.mapper.SchoolProfileMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.dto.SchoolDTO;
import com.select.school.model.entity.SchoolAdmissionScores;
import com.select.school.model.entity.SchoolProfile;
import com.select.school.service.web.WebSchoolService;
import com.select.school.utils.BeanCopierEx;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebSchoolImpl implements WebSchoolService {

    @Autowired
    private SchoolProfileMapper schoolProfileMapper;

    @Autowired
    private SchoolAdmissionScoresMapper schoolAdmissionScoresMapper;

    @Override
    public String selectAll(PagedataDto pagedata){
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();
        List<SchoolProfile> schoolProfiles = schoolProfileMapper.selectAll(SqlParameter.getParameter().addLimit(pagedata.getPageNum(),
                pagedata.getPageSize()).addQuery("schoolName",pagedata.getTitle()).getMap());
        int count = schoolProfileMapper.count(SqlParameter.getParameter().addQuery("schoolName",pagedata.getTitle()).getMap());//总数
        if (schoolProfiles == null || schoolProfiles.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<SchoolProfile> list = new ArrayList<>();
            list.addAll(schoolProfiles);
            pagedataDto.setRecords(schoolProfiles);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()+1));//页数加一 加上第一页
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
    }

    @Override
    public SchoolAdmissionScores selectSchool(int id){
        return schoolAdmissionScoresMapper.selectById(id);
    }

    //学校新增
    public int addSchool(SchoolDTO schoolDTO){
        int s = 0;
        if (schoolDTO!=null){
            SchoolProfile schoolProfile = new SchoolProfile();
            SchoolAdmissionScores admissionScores = new SchoolAdmissionScores();
            schoolProfile.setSchoolRank(String.valueOf(schoolDTO.getTwenty()));
            BeanCopierEx.copy(schoolDTO, schoolProfile);
           int p =  schoolProfileMapper.insertSchool(schoolProfile);
           if (p==0){
               return 0;
           }
            int id = schoolProfile.getId();
            schoolDTO.setSchoolId(id);
            satAcp(schoolDTO,admissionScores);
            BeanCopierEx.copy(schoolDTO, admissionScores);
           s = schoolAdmissionScoresMapper.insertSchoolDate(admissionScores);
        }else {
            return s;
        }
        return s;
    }


    private void satAcp(SchoolDTO schoolDTO,SchoolAdmissionScores admissionScores){
        int welfareWeight = schoolDTO.getWelfareWeight();//公益权重
        int competitionWeight = schoolDTO.getCompetitionWeight();//竞赛权重
        int activitiesWeight = schoolDTO.getActivitiesWeight();//课外活动权重
        int topWeight = schoolDTO.getTopWeight();//top权重
        int satWeight = schoolDTO.getSatWeight();//sat权重
        int actWeight = schoolDTO.getActWeight();//act权重
        int ibWeight = schoolDTO.getIbWeight();//ib权重
        int apWeight = schoolDTO.getApWeight();//ap权重
        int chGpaWeightStu = Integer.parseInt(schoolDTO.getChGpaWeightStu());//中国学生gpa权重
        int chStuWeightRank = Integer.parseInt(schoolDTO.getChStuWeightRank());//中国学生排名权重
        int weight = welfareWeight + competitionWeight + activitiesWeight + topWeight + chGpaWeightStu + chStuWeightRank;
        //sat_ap sat_ib act_ap act_ib 计算法
        //sat_ap: 公益权重+竞赛权重+课外活动权重+top权重+sat权重+ap权重+中国学生gpa权重+中国学生排名权重
        int sat_ap = weight + satWeight + apWeight;
        //act_ap: 公益权重+竞赛权重+课外活动权重+top权重+act权重+ap权重+中国学生gpa权重+中国学生排名权重
        int act_ap = weight + actWeight + apWeight;
        //sat_ib：公益权重+竞赛权重+课外活动权重+top权重+ib权重+sat权重+中国学生gpa权重+中国学生排名权重
        int sat_ib = weight + satWeight + ibWeight;
        //act_ib：公益权重+竞赛权重+课外活动权重+top权重+ib权重+act权重+中国学生gpa权重+中国学生排名权重
        int act_ib = weight + actWeight + ibWeight;
        admissionScores.setApAct(act_ap);
        admissionScores.setIbSat(sat_ib);
        admissionScores.setApSat(sat_ap);
        admissionScores.setIbAct(act_ib);
    }
    /**
     * 获取学校所有信息
     * @param schoolId
     * @return
     */
    public SchoolDTO findById(int schoolId){
        SchoolDTO schoolDTO  =  new SchoolDTO();
        SchoolProfile schoolProfile = schoolProfileMapper.selectById(schoolId);
        schoolDTO.setSchoolId(schoolProfile.getId());
        BeanCopierEx.copy(schoolProfile, schoolDTO);
        SchoolAdmissionScores schoolAdmissionScores = schoolAdmissionScoresMapper.selectById(schoolId);
        BeanCopierEx.copy(schoolAdmissionScores, schoolDTO);
        return schoolDTO;
    }

    /**
     * 修改学校数据的信息
     * @param schoolDTO
     * @return
     */
    public int updateSchool(SchoolDTO schoolDTO){
        int schoolId = schoolDTO.getSchoolId();
        //保存学校详情
        int schoolProfile = schoolProfileMapper.update(schoolDTO);
        if (schoolProfile == 0){
            return 0;
        }
        //根据id查询出要修改的数据
        SchoolAdmissionScores admissionScores = schoolAdmissionScoresMapper.selectById(schoolId);
        satAcp(schoolDTO,admissionScores);//计算出成绩
        BeanCopierEx.copy(schoolDTO, admissionScores);//把修改的成绩跟原来的成绩交换
        int adminScores = schoolAdmissionScoresMapper.update(admissionScores);//修改成绩 保存到数据库
        if (adminScores == 0){
            return 0;
        }
        return 1;
    }
}
