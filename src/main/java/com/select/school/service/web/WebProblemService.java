package com.select.school.service.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Option;

import java.util.List;

public interface WebProblemService {
    List<Option> findByOption(int id);
    String selectAll(PagedataDto pagedata);
}
