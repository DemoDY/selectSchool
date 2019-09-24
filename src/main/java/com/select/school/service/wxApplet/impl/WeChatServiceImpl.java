/**
 * projectName: selectSchool
 * fileName: WeChatServiceImpl.java
 * packageName: com.select.school.service.wxApplet.impl
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.service.wxApplet.impl;

import com.alibaba.fastjson.JSONObject;
import com.select.school.common.config.WechatConfig;
import com.select.school.model.entity.User;
import com.select.school.service.wxApplet.WeChatService;
import com.select.school.utils.dxm.wechat.WeChatUtil;
import com.select.school.model.dto.WeChatLoginParamDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatServiceImpl
 * @packageName: com.select.school.service.wxApplet.impl
 * @description: 小程序登录
 * @data: 2019-09-24
 **/

@Service
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private WechatConfig wechatConfig;

    @Override
    public String login(WeChatLoginParamDTO LoginVO) {

        String appid= wechatConfig.getAppid();
        String appscrect= wechatConfig.getAppscrect();
        // 调取微信登录接口获取openid
        String result = WeChatUtil.login(LoginVO.getCode(), appid, appscrect);
        JSONObject wexinResult = JSONObject.parseObject(result);
        // 保存用户信息到数据库
        try {
            User user = new User();
            user.setNickName(wexinResult.get("nickName").toString());
            user.setOpenid(wexinResult.get("openid").toString());
            user.setMobile(wexinResult.get("mobile").toString());

        }catch (Exception e){

        }

        return result;
    }
}
