package com.select.school.controller.web;

import com.select.school.service.web.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value = "login")
public class LoginController {

    @Autowired
    private ManagerService managerService;

    /**
     * 用户登录后台
     * @param userName
     * @param password
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/login")
    @ResponseBody
    public String login(String userName,String password) {
        String result = managerService.login(userName,password);
        return result;
    }
        /**
     * 用户登录后台
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/getUserInfo")
    @ResponseBody
    public String getUserInfo(String token) {
        String result = managerService.login("luanbing","luanbing");
        return result;
    }

}
