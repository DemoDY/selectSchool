package com.select.school.service.web.impl;

import com.select.school.mapper.UserMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.User;
import com.select.school.service.web.UserService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String selectAll(PagedataDto pagedata) {
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();
        List<User> userList = userMapper.selectAll(SqlParameter.getParameter().addLimit(pagedata.getPageNum(), pagedata.getPageSize()).getMap());
        int count = userMapper.count(SqlParameter.getParameter().getMap());
        if (userList == null || userList.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<User> list = new ArrayList<>();
            list.addAll(userList);
            pagedataDto.setRecords(userList);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()+1));
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
    }
}
