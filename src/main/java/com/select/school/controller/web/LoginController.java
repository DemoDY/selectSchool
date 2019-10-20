package com.select.school.controller.web;

import com.select.school.service.web.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@CrossOrigin
@Controller
@RequestMapping(value = "login")
public class LoginController {

    @Autowired
    private ManagerService managerService;

    /**
     * 用户登录后台
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/login")
    @ResponseBody
    public String login(String username,String password) {
        String result = managerService.login(username,password);
        return result;
    }
}
