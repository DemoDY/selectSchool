/**
 * projectName: selectSchool
 * fileName: WeChatAssistantUtils.java
 * packageName: com.select.school.utils.dxm.wechat
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.utils.dxm.wechat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatAssistantUtils
 * @packageName: com.select.school.utils.dxm.wechat
 * @description: 微信辅助工具
 * @data: 2019-11-13
 **/
public class WeChatAssistantUtils {
    public static String getTimestamp() {
        long timestampLong = System.currentTimeMillis();

        String timestampStr = String.valueOf(timestampLong);

        return timestampStr;
    }

    public static String getNonceStr(int length) {
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";

        Random random = new Random();

        StringBuffer randomStr = new StringBuffer();


        for (int i = 0; i < length; i++) {


            int number = random.nextInt(62);


            randomStr.append(str.charAt(number));
        }


        return randomStr.toString();
    }


    /**
     * Sign加密
     *
     * @param characterEncoding
     * @param key
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, String key, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = parameters.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (v != null && !"".equals(v) &&
                    !"sign".equals(k) && !"key".equals(k)) {
                sb.append(String.valueOf(k) + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }


    /**
     * 转XML
     *
     * @param map
     * @param sign
     * @return
     */
    public static String parseString2Xml(Map<String, Object> map, String sign) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Map.Entry<String, Object>> es = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = es.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String k = (String) entry.getKey();
            Object object = entry.getValue();
            sb.append("<" + k + ">" + object + "</" + k + ">");
        }
        if (sign != null) {
            sb.append("<sign>" + sign + "</sign>");
        }
        sb.append("</xml>");
        return sb.toString();
    }


    public static Map<String, String> parseXml(String param) throws Exception {
        if (param == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(param);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        return map;
    }


    public static Map<String, String> parseXml1(String xml) throws Exception {
        Document doc = DocumentHelper.parseText(xml);
        Element rootElement = doc.getRootElement();

        Iterator<Element> elements = rootElement.elementIterator();
        Map<String, String> map = new HashMap<String, String>();
        while (elements.hasNext()) {
            Element element = elements.next();
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

    /**
     * 根据长度生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
