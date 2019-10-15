package com.select.school.controller.web;

import com.select.school.model.entity.User;
import com.select.school.service.web.UserService;
import com.select.school.utils.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/userList")
    @ResponseBody
    public AjaxResult selectAll(){
        AjaxResult ajaxResult = userService.selectAll();
        return ajaxResult;
    }
}
