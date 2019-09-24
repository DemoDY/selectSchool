package com.select.school.controller.wxApplet;

import com.select.school.service.wxApplet.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;


}
