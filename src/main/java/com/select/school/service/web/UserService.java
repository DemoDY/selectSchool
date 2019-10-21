package com.select.school.service.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.utils.dxm.result.ResponseResult;

public interface UserService {
    String selectAll(PagedataDto pagedata);
}
