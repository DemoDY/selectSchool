/**
 * projectName: selectSchool
 * fileName: WeChatController.java
 * packageName: com.select.school.controller
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.controller.wxApplet;

import com.select.school.model.dto.WeChatLoginParamDTO;
import com.select.school.service.wxApplet.WeChatService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatController
 * @packageName: com.select.school.controller
 * @description: 小程序相关接口
 * @data: 2019-09-24
 **/

@RestController
@RequestMapping(value = "/wechat")
public class WeChatController {
    @Resource
    private WeChatService weChatService;

    /*
     * 微信小程序登录
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public String login(@RequestBody WeChatLoginParamDTO login) {
        if (login.getCode() == null || login.getCode().equals("") || login.getCode().equals("null")) {
            return ResponseUtil.setResult(ResponseCode.REQUEST_NOT);
        }
        String response = weChatService.login(login);
        return response;
    }
}
