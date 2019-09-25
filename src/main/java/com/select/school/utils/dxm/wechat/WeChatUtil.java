package com.select.school.utils.dxm.wechat;

import com.select.school.utils.dxm.result.ResponseCode;
import com.sun.tools.javadoc.Start;


/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatUtil
 * @packageName: com.select.school.utils.dxm.wechat
 * @description: 微信工具类
 * @data: 2019-09-24
 **/

public class WeChatUtil {

    /**
     * 小程序登录
     */
    public static String login(String appid, String appSecret, String code) {
        String url = String.format(WeChatAPIUrls.USER_LOGIN, appid, appSecret, code);
        String result = RequestGetUtil.sendPOSTString(url, "");

        return result;
    }

    public static void main(String[] args) {
        String code ="011IRFwc118JYv0MW8xc1pACwc1IRFwH";
//        appid: wx1330df3ec7f2cac0
//        appscrect: 0ddba9f0f9ef6eb42c2e8b6e37b78fc3
        String url = String.format(WeChatAPIUrls.USER_LOGIN, "wx1330df3ec7f2cac0", "0ddba9f0f9ef6eb42c2e8b6e37b78fc3", code);
        String result = RequestGetUtil.sendPOSTString(url, "");
        System.out.println(result);
    }

    /**
     * 公众号根据code获取openId
     *//*
	public static WeChatTempAccessTokenResponse getOpenid(String code, String appid, String appSecret){
		String url = String.format(WeChatAPIUrls.USER_GET_OPENID,appid,appSecret,code);

		String result = RequestGetUtil.sendPOSTString(url, "");
		WeChatTempAccessTokenResponse response = new WeChatTempAccessTokenResponse(result);

		response.setCodeMsg(ResponseCode.SUCCESS);
		return response;
	}
	*//**
     * 公众号根据openId获取用户信息
     *//*
	public static WeChatUserInfoDetailResponse getUserInfo(String openId, String appid, String appSecret){
		WeChatAPIResponse wx = getAccessToken(appid,appSecret);
		String url = String.format(WeChatAPIUrls.USER_GET_USERINFO,wx.getAccessToken(), openId);
		String result = RequestGetUtil.sendPOSTString(url, "");
		
		WeChatUserInfoDetailResponse response = new WeChatUserInfoDetailResponse(result);

		response.setCodeMsg(ResponseCode.SUCCESS);
		return response;
	}
	*//**
     * 公众号获取accessToken
     *//*
	public static WeChatAPIResponse getAccessToken(String appid, String appSecret){
		WeChatAPIResponse back = RequestGetUtil.sendPOST(String.format(WeChatAPIUrls.GET_TOKEN, appid, appSecret),"");
		return back;
	}*/
}