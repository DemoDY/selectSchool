package com.select.school.service.web.impl;

import com.select.school.mapper.ManagerMapper;
import com.select.school.model.entity.Manager;
import com.select.school.service.web.ManagerService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerImpl implements ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public String login(String username, String password ) {
        if (username.equals("")) {
            //用户不存在
            return ResponseUtil.setResult(ResponseCode.ACCOUNT_NOT);
        }
        if (password.equals("")) {
            //密码不为空
            return ResponseUtil.setResult(ResponseCode.PASSWORD_NOT);
        }
        Manager manager = managerMapper.findByLogin(username);
        if (manager != null) {
            String pass = manager.getPassword();
            if (!pass.equals(password)) {
                //密码不正确
                return ResponseUtil.setResult(ResponseCode.PASSWORD_ERROR);
            }
        } else {
            return ResponseUtil.setResult(ResponseCode.ACCOUNT_NOT);
        }
        return ResponseUtil.setResult(ResponseCode.SUCCESS,manager);
    }
}
