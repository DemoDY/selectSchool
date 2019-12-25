package com.select.school.controller.web;

import com.select.school.model.dto.PagedataDto;
import com.select.school.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/userList")
    @ResponseBody
    public String selectAll(@RequestBody PagedataDto pagedata){
        String result = userService.selectAll(pagedata);
        return result;
    }
}
