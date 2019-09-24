package com.select.school.utils.dxm.wechat;


/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatAPIUrls
 * @packageName: com.select.school.utils.dxm.wechat
 * @description: 微信url配置信息
 * @data: 2019-09-24
 **/
public class WeChatAPIUrls {

	/**
	 * token
	 */
	public static String GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	public static String GET_JS_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	
	/**
	 * menu
	 */
	public static String MENU_CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
	
	/**
	 * user
	 */
	public static String USER_GET_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	public static String USER_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";


	/**
	 * 小程序
	 */
	public static String USER_LOGIN = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
	public static String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
}