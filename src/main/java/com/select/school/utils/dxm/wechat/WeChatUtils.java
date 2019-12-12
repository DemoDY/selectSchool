package com.select.school.utils.dxm.wechat;


//import com.select.school.model.vo.WxPayVo;
import com.select.school.model.vo.WxPayVo;
import com.select.school.model.vo.WxTradeDetail;
import com.select.school.model.vo.WxTradeSummary;
import com.select.school.utils.DateUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

        import java.util.*;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatUtils
 * @packageName: com.select.school.utils.dxm.wechat
 * @description: 微信工具类
 * @data: 2019-09-24
 **/

public class WeChatUtils {

    private static final Logger logger = LoggerFactory.getLogger(WeChatUtils.class);

    /**
     * 小程序登录
     */
    public static String login(String appid, String appSecret, String code) {
        String url = String.format(WeChatAPIParams.USER_LOGIN, appid, appSecret, code);
        String result = RequestGetUtil.sendPOSTString(url, "");
        return result;
    }

    /**
     * 微信支付统一下单
     */
    public static Object wxPay(WxPayVo wxPayVo) {

        //签名 并调取微信统一下单
        SortedMap<String, Object> parameter = new TreeMap<String, Object>();
        parameter.put("appid", WeChatAPIParams.WECHAR_PAY_APPID);                    //  app_id
        parameter.put("mch_id", WeChatAPIParams.WECHAR_PAY_MCH_ID);                    // 商户号 mch_id
        parameter.put("device_info", wxPayVo.getDeviceInfo());                        // 设备号 device_info
        parameter.put("openid", wxPayVo.getOpenid());                                // 用户标识
        parameter.put("body", wxPayVo.getBody());                                // 商品描述 body
        parameter.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));       // 随机字符串 nonce_str 16位
        parameter.put("out_trade_no", wxPayVo.getDeviceInfo());                                // 商户订单号 out_trade_no
        parameter.put("total_fee", (int) (wxPayVo.getTotalFee() * 100));    // 总金额 total_fee
        parameter.put("spbill_create_ip", wxPayVo.getSpbillCreateIp());                         // 终端IP spbill_create_ip
        parameter.put("trade_type", "JSAPI");                                                    // 交易类型 trade_type   APP
        parameter.put("notify_url", WeChatAPIParams.NOTIFY_URL);                            // 通知地址 notify_url
        // 签名 sign(MD5)
        String sign = WeChatAssistantUtils.createSign("UTF-8", WeChatAPIParams.KEY, parameter);
        System.out.println("--sign:"+sign);
        // 把数据转为xml
        String param = WeChatAssistantUtils.parseString2Xml(parameter, sign);
        String url = WeChatAPIParams.WECHAR_PAY;
        // 将解析结果存储在HashMap中
        String wxResult;
        try {
            //发送请求
            wxResult = HttpUtils.doPost(url, param);
            return JSONObject.fromObject(WeChatAssistantUtils.parseXml(wxResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载对账单
     * @return
     * @throws Exception
     */
    public static Boolean downloadFile() throws Exception {
        String billDate = DateUtil.getYesterday("yyyyMMdd");
        //签名
        SortedMap<String, Object> parameter = new TreeMap<String, Object>();
        parameter.put("appid", WeChatAPIParams.WECHAR_PAY_APPID);                    // 商户号 app_id
        parameter.put("mch_id", WeChatAPIParams.WECHAR_PAY_MCH_ID);                    // 应用ID mch_id
        parameter.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串 nonce_str
        parameter.put("bill_date", billDate);        // 下载对账单的日期，格式：20140603


        // 签名 sign(MD5)
        String sign = WeChatAssistantUtils.createSign("UTF-8", WeChatAPIParams.KEY, parameter);
        // 把数据转为xml
        String param = WeChatAssistantUtils.parseString2Xml(parameter, sign);
        String billUrl = WeChatAPIParams.DOWNLOAD_BILL_URL;
        // 获取下载信息
        String result = HttpUtils.doPost(billUrl, param);

        if (StringUtils.hasText(result) && !result.contains("error_code")) {
            List<WxTradeDetail> wxTradeDetailList = new ArrayList<WxTradeDetail>();
            String[] str = result.split("\n");//按行读取数据（*这个尤为重要*）
            int len = str.length;
            for (int i = 0; i < len; i++) {
                String[] tradeDetailArray = str[i].replace("`", "").split(",");
                if (i > 0 && i < (len - 2)) {
                    // 明细行数据[交易时间,公众账号ID,商户号,特约商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,应结订单金额,代金券金额,商品名称,商户数据包,手续费,费率,订单金额,费率备注]
                    WxTradeDetail entity = new WxTradeDetail();
                    entity.setBillDate(billDate);
                    entity.setTransDate(getArrayValue(tradeDetailArray, 0));// 交易时间
                    entity.setCommonId(getArrayValue(tradeDetailArray, 1));// 公众账号ID
                    entity.setBusinessNo(getArrayValue(tradeDetailArray, 2));// 商户号
                    entity.setChildBusinessNo(getArrayValue(tradeDetailArray, 3));// 特约商户号
                    entity.setEquipmentNo(getArrayValue(tradeDetailArray, 4));// 设备号
                    entity.setWxOrderNo(getArrayValue(tradeDetailArray, 5));// 微信订单号
                    entity.setBusinessOrderNo(getArrayValue(tradeDetailArray, 6));// 商户订单号
                    entity.setUserIdentity(getArrayValue(tradeDetailArray, 7));// 用户标识
                    entity.setTransType(getArrayValue(tradeDetailArray, 8));// 交易类型
                    entity.setTransStatus(getArrayValue(tradeDetailArray, 9));// 交易状态
                    entity.setPaymentBank(getArrayValue(tradeDetailArray, 10));// 付款银行
                    entity.setCurrency(getArrayValue(tradeDetailArray, 11)); // 货币种类
                    entity.setSettleAccounts(getArrayValue(tradeDetailArray, 12)); // 应结订单金额
                    entity.setCoupon(getArrayValue(tradeDetailArray, 13)); // 代金券金额
                    entity.setBusinessName(getArrayValue(tradeDetailArray, 14));// 商品名称
                    entity.setBusinessData(getArrayValue(tradeDetailArray, 15));// 商户数据包
                    entity.setFee(getArrayValue(tradeDetailArray, 16));// 手续费
                    entity.setRate(getArrayValue(tradeDetailArray, 17));// 费率
                    entity.setOrderAmount(getArrayValue(tradeDetailArray, 18));// 订单金额
                    entity.setRateRemark(getArrayValue(tradeDetailArray, 19));// 费率备注
                    wxTradeDetailList.add(entity);
                }

                // 汇总行数据 [总交易单数,应结订单总金额,退款总金额,充值券退款总金额,手续费总金额,订单总金额,申请退款总金额]
                if (i > (len - 2)) {
                    WxTradeSummary wxTradeSummary = new WxTradeSummary();
                    wxTradeSummary.setBillDate(billDate);
                    wxTradeSummary.setTradeCount(getArrayValue(tradeDetailArray, 0));// 总交易单数
                    wxTradeSummary.setShouldSettleAmount(getArrayValue(tradeDetailArray, 1));// 应结订单总金额
                    wxTradeSummary.setRefundAmount(getArrayValue(tradeDetailArray, 2));// 退款总金额
                    wxTradeSummary.setRechargeVoucherAmount(getArrayValue(tradeDetailArray, 3));// 充值券退款总金额
                    wxTradeSummary.setServiceCharge(getArrayValue(tradeDetailArray, 4));// 手续费总金额
                    wxTradeSummary.setTotalOrderAmount(getArrayValue(tradeDetailArray, 5));// 订单总金额
                    wxTradeSummary.setApplyRefundAmount(getArrayValue(tradeDetailArray, 6));// 申请退款总金额
                    //save(wxTradeSummary);
                }
            }
            //batchSave(wxTradeDetailList);
        } else {
            System.out.println("###########获取对账数据有误#################");
            System.out.println("返回对账数据为:" + result);
        }
        return false;
    }

    public static String getArrayValue(String[] tradeDetailArray, int index) {
        try {
            return tradeDetailArray[index];
        } catch (Exception e) {
            return "";
        }
    }
}