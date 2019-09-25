package com.select.school.service.wxApplet;


import com.select.school.model.dto.WeChatLoginParamDTO;

public interface WeChatService {

    // 微信小程序登录
    public String login(WeChatLoginParamDTO LoginVO);

}
