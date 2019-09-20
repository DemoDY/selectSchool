package com.select.school.service.impl;

import com.select.school.service.SchoolAdmissionScoresService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolAdmissionScoresImpl implements SchoolAdmissionScoresService {
}
