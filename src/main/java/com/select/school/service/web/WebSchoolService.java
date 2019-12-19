package com.select.school.service.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.dto.SchoolDTO;
import com.select.school.model.entity.SchoolAdmissionScores;


public interface WebSchoolService {

    String selectAll(PagedataDto pagedata,String title);

    SchoolAdmissionScores selectSchool(int id);

    int addSchool(SchoolDTO schoolDTO);

    SchoolDTO findById(int schoolId);
    int updateSchool(SchoolDTO schoolDTO);
}
