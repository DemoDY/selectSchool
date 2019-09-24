package com.select.school.utils.dxm.wechat;

import net.sf.json.JSONObject;

import java.util.Map;

public class TemplateMessageUtil {
/*	//https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=
	private static String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
	
	*//**
	 * 开奖时间{{keyword1.DATA}}
	 * 开奖期数{{keyword2.DATA}}
	 * 活动商品{{keyword3.DATA}}
	 *//*
	public JSONObject sendPrizeTemplateMessage(String openId,String time,String cycleNumber,String treasureName,String accessToken) {
		JSONObject param = new JSONObject();
		param.put("access_token", accessToken);
		param.put("touser", openId);
		param.put("template_id", "xuasgGpxb5fg29EWYkrW-2zzuxT9LS2jbG7vuecXZnQ");
		param.put("form_id", "form_id");

		Map<String,Object> keyword1 = MapControl.getControl().put("value", time).getMap();
		Map<String,Object> keyword2 = MapControl.getControl().put("value", cycleNumber).getMap();
		Map<String,Object> keyword3 = MapControl.getControl().put("value", treasureName).getMap();
		
		param.put("data", JSONObject.fromObject(MapControl.getControl()
				.put("keyword1", keyword1)
				.put("keyword2", keyword2)
				.put("keyword3", keyword3)
				.getMap()));
		
		String result = RequestGetUtil.sendPOSTString(url+accessToken, param.toString());
		return JSONObject.fromObject(result);
	}*/
}