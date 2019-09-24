package com.select.school.service.wxApplet;


import com.select.school.vo.WeChatLoginParamVO;

public interface WeChatService {

    // 微信小程序登录
    public String login(WeChatLoginParamVO LoginVO);


}
