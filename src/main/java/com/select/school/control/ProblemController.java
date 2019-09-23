package com.select.school.control;

import com.select.school.model.entity.Problem;
import com.select.school.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;


}
