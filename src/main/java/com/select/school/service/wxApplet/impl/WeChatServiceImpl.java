/**
 * projectName: selectSchool
 * fileName: WeChatServiceImpl.java
 * packageName: com.select.school.service.wxApplet.impl
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.service.wxApplet.impl;

import com.alibaba.fastjson.JSONObject;
import com.select.school.config.WechatConfig;
import com.select.school.mapper.UserMapper;
import com.select.school.model.entity.User;
import com.select.school.service.wxApplet.WeChatService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseUtil;
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
    @Resource
    private UserMapper userMapper;

    @Override
    public String login(WeChatLoginParamDTO login) {
        try {
            String appid= wechatConfig.getAppid();
            String appscrect= wechatConfig.getAppscrect();

            // 调取微信登录接口获取openid
            String result = WeChatUtil.login( appid, appscrect,login.getCode());

            System.out.println(result);
            JSONObject wexinResult = JSONObject.parseObject(result);
            // 保存用户信息到数据库
            if (result.contains("openid")){
                User user = new User();
                user.setNickName(login.getNickName());
                user.setOpenid(wexinResult.get("openid").toString());
                user.setMobile(login.getMoblie());
                user.setAvatarUrl(login.getAvatarUrl());
                int i = userMapper.create(user);
                System.out.println("创建："+i);
            }else {
                return ResponseUtil.setResult(ResponseCode.REQUEST_NOT, result);
            }
            return ResponseUtil.setResult(ResponseCode.SUCCESS, result);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.setResult(ResponseCode.ERROR);
        }
    }
}
