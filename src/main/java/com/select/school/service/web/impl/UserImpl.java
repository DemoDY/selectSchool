package com.select.school.service.web.impl;

import com.select.school.mapper.UserMapper;
import com.select.school.model.entity.User;
import com.select.school.service.web.UserService;
import com.select.school.utils.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public AjaxResult selectAll() {
        AjaxResult ajaxResult = new AjaxResult();
        List<User> userList = userMapper.selectAll();
        ajaxResult.put("userlist",userList);
        return ajaxResult;
    }
}
