/**
 * projectName: selectSchool
 * fileName: WeChatAssistantUtils.java
 * packageName: com.select.school.utils.dxm.wechat
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.utils.dxm.wechat;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

    /**
     * 获取时间戳
     */
    public static String getTimestamp() {
        long timestampLong = System.currentTimeMillis();
        return String.valueOf(timestampLong);
    }

    /**
     * 获取随机字符串
     *
     */
    public static String getNonceStr(int length) {
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            randomStr.append(str.charAt(number));
        }
        return randomStr.toString();
    }


    /**
     * Sign加密
     */
    public static String createSign(String key, SortedMap<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> es = parameters.entrySet();
        for (Map.Entry<String, Object> entry : es) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (v != null && !"".equals(v) &&
                    !"sign".equals(k) && !"key".equals(k)) {
                sb.append(String.valueOf(k)).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(key);
        return MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
    }


    /**
     * 转XML
     */
    public static String parseString2Xml(Map<String, Object> map, String sign) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Set<Map.Entry<String, Object>> es = map.entrySet();
        for (Map.Entry<String, Object> entry : es) {
            String k = entry.getKey();
            Object object = entry.getValue();
            sb.append("<").append(k).append(">").append(object).append("</").append(k).append(">");
        }
        if (sign != null) {
            sb.append("<sign>").append(sign).append("</sign>");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 解析XML
     */
    static Map<String,Object> doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if ("".equals(strxml)) {
            return null;
        }
        Map<String, Object> m = new HashMap<>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes(StandardCharsets.UTF_8));
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document doc = builder.build(in);
        org.jdom.Element root = doc.getRootElement();
        List list = root.getChildren();
        for (Object o : list) {
            org.jdom.Element e = (org.jdom.Element) o;
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            m.put(k, v);
        }
        //关闭流
        in.close();
        return m;
    }

    /**
     * List
     */
    private static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            for (Object child : children) {
                org.jdom.Element e = (org.jdom.Element) child;
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<").append(name).append(">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</").append(name).append(">");
            }
        }
        return sb.toString();
    }


//    public static Map<String, String> parseXml(String xml) throws Exception {
//        Document doc = DocumentHelper.parseText(xml);
//        Element rootElement = doc.getRootElement();
//
//        Iterator<Element> elements = rootElement.elementIterator();
//        Map<String, String> map = new HashMap<String, String>();
//        while (elements.hasNext()) {
//            Element element = elements.next();
//            map.put(element.getName(), element.getText());
//        }
//        return map;
//    }

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
}

