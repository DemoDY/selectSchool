package com.select.school.utils.dxm.wechat;

import com.select.school.utils.dxm.result.ResponseResult;
import net.sf.json.JSONObject;

public class WeChatTempAccessTokenResponse extends ResponseResult {

	public WeChatTempAccessTokenResponse(String json){
		JSONObject a = JSONObject.fromObject(json);
		setAccess_token(a.has("access_token")?a.getString("access_token"):"");
		setExpires_in(a.has("expires_in")?a.getInt("expires_in"):0);
		setRefresh_token(a.has("refresh_token")?a.getString("refresh_token"):"");
		setOpenid(a.has("openid")?a.getString("openid"):"");
		setScope(a.has("scope")?a.getString("scope"):"");
		setUnionid(a.has("unionid")?a.getString("unionid"):"");
		
		setErrcode(a.has("errcode")?a.getInt("errcode"):0);
		setErrmsg(a.has("errmsg")?a.getString("errmsg"):"");
		setSessionKey(a.has("session_key")?a.getString("session_key"):"");
	}
	public WeChatTempAccessTokenResponse(){
		
	}
	private int userId;
	private String access_token;
	private int expires_in;
	private String refresh_token;
	private String sessionKey;
	private String openid;
	private String scope;
	private String unionid;

	private String nickName;
	private String	userName;
	private int gender;

	private int errcode;
	private String errmsg;

	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
}