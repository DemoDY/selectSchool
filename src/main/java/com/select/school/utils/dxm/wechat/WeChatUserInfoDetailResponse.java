package com.select.school.utils.dxm.wechat;

import com.select.school.utils.dxm.result.ResponseResult;
import net.sf.json.JSONObject;

public class WeChatUserInfoDetailResponse extends ResponseResult {

	public WeChatUserInfoDetailResponse(String json){
		JSONObject a = JSONObject.fromObject(json);
		setOpenId(a.has("openId")?a.getString("openId"):"");
		setNickName(a.has("nickName")?a.getString("nickName"):"");
		setProvince(a.has("province")?a.getString("province"):"");
		setCity(a.has("city")?a.getString("city"):"");
		setCountry(a.has("country")?a.getString("country"):"");
		setAvatarUrl(a.has("avatarUrl")?a.getString("avatarUrl"):"");
		setGender(a.has("gender")?a.getInt("gender"):0);
		setErrcode(a.has("errcode")?a.getInt("errcode"):0);
		setErrmsg(a.has("errmsg")?a.getString("errmsg"):"");
		setUnionId(a.has("unionId")?a.getString("unionId"):"");
	}
	public WeChatUserInfoDetailResponse(){
		
	}
	
	private String openId;
	private String nickName;
	private String avatarUrl;
	private String unionId;
	private String errmsg;
	private int errcode;
	private int gender;
	private String language;
	private String city;
	private String province;
	private String country;
	private String watermark;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
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
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWatermark() {
		return watermark;
	}
	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}
}