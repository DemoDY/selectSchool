package com.select.school.control;

import com.select.school.bean.AjaxResult;
import com.select.school.model.dto.OptionDTO;
import com.select.school.model.entity.Option;
import com.select.school.model.vo.OptionVo;
import com.select.school.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/option")
@RestController
public class OptionController {
    private static Logger logger = LoggerFactory.getLogger(OptionController.class);

    @Autowired
    private OptionService optionService;

    /**
     * @param optionVo
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/option-receive")
    @ResponseBody
    public AjaxResult optionReceive(@RequestBody OptionVo optionVo) {
        try {
            List<OptionDTO> options = optionVo.getOptionList();
            // 执行批量保存
            optionService.selectOption(options);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(900, "操作失败：系统错误");
        }
        return AjaxResult.success(AjaxResult.CODE_SUCCESS, "操作成功");
    }
}
