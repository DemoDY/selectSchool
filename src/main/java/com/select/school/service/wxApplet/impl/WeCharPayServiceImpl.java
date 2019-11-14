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
import com.select.school.utils.dxm.wechat.WeChatAssistantUtils;
import com.select.school.utils.dxm.wechat.WeChatUtils;
import net.sf.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private WxAffirmMapper wxAffirmMapper;

    @Override
    public String wxPay(WxPayVo wxPayVo) {

        ResponseResult result = new ResponseResult();
        try {
            Order order = new Order();
            order.setOrderNumber(WeChatAssistantUtils.getOrderIdByTime());
            order.setState(1);
            order.setTotalFee(wxPayVo.getTotalFee());
            order.setOrderNumber(wxPayVo.getTradeType());
            orderMapper.create(order);

            // 统一下单
            Object wxPay = WeChatUtils.wxPay(wxPayVo);

            if (wxPay != null) {
                JSONObject payResulet = JSONObject.fromObject(wxPay);
                if (!payResulet.get("return_code").equals("SUCCESS")) {
                    result.setCode(401);
                    result.setMsg(payResulet.getString("return_msg"));
                    // 修改订单状态
                    orderMapper.update(SqlParameter.getParameter().addUpdate("state", 2).addQuery("order_number",order.getOrderNumber()).getMap());
                } else {
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
     * 回调通知
     *
     * @param request
     * @return
     */
    @Override
    public Object affirm(HttpRequest request) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("return_code", "SUCCESS");
        resultMap.put("return_msg", "OK");
        Object param = WeChatAssistantUtils.parseString2Xml(resultMap, null);

        //解析微信返回通知的xml数据
        Map<String, String> respance = null;
        try {
            respance = WeChatAssistantUtils.parseXml1(request.toString());
            JSONObject result = JSONObject.fromObject(respance);

            // 将数据保存至数据库
            if (result != null && result.get("return_code").equals("SUCCESS")) {

                if (result.get("total_fee") != null) {
                    Double totalFee = Double.parseDouble(result.get("total_fee").toString()) / 100;
                }
                WxAffirm detail = wxAffirmMapper.detail(SqlParameter.getParameter()
                        .addQuery("transaction_id", result.get("transaction_id"))
                        .getMap());

                // 如果存在数据 直接返回
                if (detail != null) {
                    System.out.println("======>>>重复订单--不做插入");

                } else {
                    //修改订单状态
                    orderMapper.update(SqlParameter.getParameter().addUpdate("state", 3)
                            .addQuery("transaction_id",result.get("transaction_id"))
                            .getMap());
                    //插入回调记录
                    WxAffirm wxAffirm = new WxAffirm();
                    wxAffirm.setOutTradeNo(result.getString("out_trade_no"));
                    wxAffirm.setTransactionId(result.getString("transaction_id"));
                    wxAffirm.setOpenid(result.getString("openid"));
                    wxAffirm.setTotalFee(Double.valueOf(Integer.valueOf(result.getString("total_fee"))/100));
                    wxAffirm.setBankType(result.getString("bank_type"));
                    wxAffirm.setCashFee(Double.valueOf(Integer.valueOf(result.getString("cash_fee"))/100));
                    wxAffirm.setIsSubscribe(result.getString("is_subscribe"));
                    wxAffirm.setTradeType(result.getString("trade_type"));
                    wxAffirm.setResultCode(result.getString("result_code"));
                    wxAffirm.setTimeEnd(result.getString("time_end"));
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
