package com.select.school.utils.dxm.wechat;

import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatAPIResponse
 * @packageName: om.select.school.utils.dxm.wechat
 * @description: 微信返回参数
 * @data: 2019-09-24
 **/
public class WeChatAPIResponse {
	private Date accessTokenTime;
	private String accessToken;
	private String jsapiTicket;
	private int expiresIn = 7200;
	private String errmsg;
	private int errcode;
	private int hasExpired;
	
	public WeChatAPIResponse(String json) {
		JSONObject a = JSONObject.fromObject(json);
		setAccessToken(a.has("access_token")?a.getString("access_token"):"");
		setExpiresIn(a.has("expires_in")?a.getInt("expires_in"):0);
		setErrcode(a.has("errcode")?a.getInt("errcode"):0);
		setErrmsg(a.has("errmsg")?a.getString("errmsg"):"");
		setJsapiTicket(a.has("ticket")?a.getString("ticket"):"");
		setAccessTokenTime(new Date());
		setHasExpired(0);
	}
	public int getHasExpired() {
		setHasExpired(0);
		if (accessTokenTime != null){
			accessTokenTime.getTime();
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(accessTokenTime);
        	cal.set(Calendar.SECOND,expiresIn);
        	
            if (new Date().before(cal.getTime())){
                setHasExpired(1);
            }
        }
        return hasExpired;
	}
	public void setHasExpired(int hasExpired) {
	}
	public String getJsapiTicket() {
		return jsapiTicket;
	}
	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}
	public Date getAccessTokenTime() {
		return accessTokenTime;
	}
	public void setAccessTokenTime(Date accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
}