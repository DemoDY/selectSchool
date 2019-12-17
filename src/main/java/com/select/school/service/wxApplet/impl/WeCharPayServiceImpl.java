/**
 * projectName: selectSchool
 * fileName: WeCharPayServiceImpl.java
 * packageName: com.select.school.service.wxApplet.impl
 * date: 2019-11-12
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.service.wxApplet.impl;

import com.select.school.mapper.OrderMapper;
import com.select.school.mapper.WxAffirmMapper;
import com.select.school.model.entity.Order;
import com.select.school.model.entity.WxAffirm;
import com.select.school.model.vo.WxPayVo;
import com.select.school.service.wxApplet.WeCharPayService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.dxm.wechat.WeChatAPIParams;
import com.select.school.utils.dxm.wechat.WeChatAssistantUtils;
import com.select.school.utils.dxm.wechat.WeChatUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeCharPayServiceImpl
 * @packageName: com.select.school.service.wxApplet.impl
 * @description: 微信支付
 * @data: 2019-11-12
 **/
@Service
public class WeCharPayServiceImpl implements WeCharPayService {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private WxAffirmMapper wxAffirmMapper;

    @Override
    public String wxPay(WxPayVo wxPayVo) {
        ResponseResult result = new ResponseResult();
        try {
            Order order = new Order();
            order.setOrderNumber(wxPayVo.getOrderNumber());//订单号
            order.setState(1);
            order.setTotalFee(wxPayVo.getTotalFee());
            order.setOpenid(wxPayVo.getOpenid());
            order.setCreateTime(df.format(new Date()));
            orderMapper.create(order);
            // 统一下单
            Object wxPay = WeChatUtils.wxPay(wxPayVo);
            if (wxPay != null) {
                JSONObject payResulet = JSONObject.fromObject(wxPay);
                String timeStamp = String.valueOf(System.currentTimeMillis()/1000);

                if (!payResulet.get("return_code").equals("SUCCESS")) {
                    result.setCode(401);
                    result.setMsg(payResulet.getString("return_msg"));
                } else {
                    //小程序二次签名
                    SortedMap<String, Object> parameter = new TreeMap<String, Object>();
                    parameter.put("appId", WeChatAPIParams.WECHAR_PAY_APPID);                      //  app_id
                    parameter.put("timeStamp",timeStamp);
                    parameter.put("nonceStr", RandomStringUtils.randomAlphanumeric(16));    // 随机字符串 nonce_str 16位
                    parameter.put("package","prepay_id=" + payResulet.get("prepay_id"));
                    parameter.put("signType","MD5");
                    // 签名 sign(MD5)
                    String sign = WeChatAssistantUtils.createSign(WeChatAPIParams.KEY, parameter);

                    parameter.put("paySign",sign);
                    result.setData(parameter);
                    result.setCodeMsg(ResponseCode.SUCCESS);
                }
            } else {
                result.setCodeMsg(ResponseCode.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCodeMsg(ResponseCode.ERROR);
        }
        return JSONObject.fromObject(result).toString();
    }

    /**
     * 本地支付回调
     *
     * @param wxPayVo
     * @return
     */
    @Override
    public String payEnd(WxPayVo wxPayVo) {
        // 修改订单状态  2:已支付
        orderMapper.update(SqlParameter.getParameter().addUpdate("state", 2)
                .addQuery("orderNumber", wxPayVo.getOrderNumber())
                .addQuery("openid", wxPayVo.getOpenid())
                .getMap());
        ResponseResult result = new ResponseResult();
        result.setCodeMsg(ResponseCode.SUCCESS);
        return JSONObject.fromObject(result).toString();
    }


    /**
     * 回调通知
     */
    @Override
    public Object affirm(HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("return_code", "SUCCESS");
        resultMap.put("return_msg", "OK");
        Object param = WeChatAssistantUtils.parseString2Xml(resultMap, null);

        //解析微信返回通知的xml数据
        try {
            Map<String, Object> map = WeChatUtils.getXML(request);
            JSONObject result = JSONObject.fromObject(map);
            String re = result.getString("out_trade_no");
            System.out.println(re);
            // 将数据保存至数据库
            if (result != null && result.get("return_code").equals("SUCCESS")) {
                WxAffirm detail = wxAffirmMapper.detail(SqlParameter.getParameter()
                        .addQuery("orderNumber", re)
                        .getMap());
                // 如果存在数据 直接返回
                if (detail != null) {
                    System.out.println("======>>>重复订单--不做插入");
                } else {
                    //修改订单状态
                    orderMapper.update(SqlParameter.getParameter().addQuery("updateState", 3).addQuery("updateTime",df.format(new Date()))
                            .addQuery("orderNumber",re).getMap());
                    //插入回调记录
                    WxAffirm wxAffirm = new WxAffirm();
                    wxAffirm.setOutTradeNo(result.getString("out_trade_no"));
                    wxAffirm.setTransactionId(result.getString("transaction_id"));
                    wxAffirm.setOpenid(result.getString("openid"));
                    wxAffirm.setTotalFee(result.getDouble("total_fee") / 100);
                    wxAffirm.setBankType(result.getString("bank_type"));
                    wxAffirm.setCashFee(result.getDouble("cash_fee") / 100);
                    wxAffirm.setIsSubscribe(result.getString("is_subscribe"));
                    wxAffirm.setTradeType(result.getString("trade_type"));
                    wxAffirm.setResultCode(result.getString("result_code"));
                    wxAffirm.setTimeEnd(result.getString("time_end"));
                    System.out.println(wxAffirm);
                    wxAffirmMapper.create(wxAffirm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return param;
    }


}
