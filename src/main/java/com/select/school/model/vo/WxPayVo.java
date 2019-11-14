/**
 * projectName: selectSchool
 * fileName: WxPayVo.java
 * packageName: com.select.school.model.vo
 * date: 2019-11-12
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.vo;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WxPayVo
 * @packageName: com.select.school.model.vo
 * @description:
 * @data: 2019-11-12
 **/
public class WxPayVo {

    private String productId;       // 商品ID
    private String body;            // 商品描述
    private String deviceInfo;      // 设备号
    private String openid;          // 用户标识
    private double totalFee;        // 标价金额
    private String spbillCreateIp;  // 终端IP
    private String timeStart;       // 交易起始时间
    private String tradeType;       // 交易类型 	JSAPI


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
