package com.select.school.utils.dxm.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: ResponseCode
 * @packageName: com.fc.aden.util.dxm.result
 * @description: CODE MSG枚举
 * @data: 2019-09-24
 **/
public class RequestGetUtil {
	public static WeChatAPIResponse sendPOST(String requestUrl, String jsonString){
    	String result = "";
    	String output = "error";
    	
		try {
			URL url = new URL(requestUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes("utf-8"));
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"utf-8"));
	 
			while ((result = br.readLine()) != null) {
				output = result;
			}
	 
			conn.disconnect();

			return new WeChatAPIResponse(output);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
	public static String sendPOSTString(String requestUrl,String jsonString){
    	String result = "";
    	String output = "";
    	
		try {
			URL url = new URL(requestUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes("utf-8"));
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"utf-8"));
	 
			while ((result = br.readLine()) != null) {
				output = output + result;
			}
	 
			conn.disconnect();

			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }

	public static String sendGETString(String requestUrl){
    	String result = "";
    	String output = "";
    	
		try {
			URL url = new URL(requestUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"utf-8"));
	 
			while ((result = br.readLine()) != null) {
				output = output + result;
			}
	 
			conn.disconnect();

			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
