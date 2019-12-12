/**
 * projectName: selectSchool
 * fileName: WxAffirm.java
 * packageName: com.select.school.model.entity
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.entity;

import java.io.Serializable;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WxAffirm
 * @packageName: com.select.school.model.entity
 * @description: 微信回调反馈
 * @data: 2019-11-13
 **/
public class WxAffirm implements Serializable {
    private String transactionId;  //微信支付订单号
    private String outTradeNo;     //商户订单号
    private String resultCode;     //业务结果
    private String openid;         //用户标识
    private String isSubscribe;    //是否关注公众账号
    private String tradeType;      //交易类型
    private String bankType;       //付款银行
    private Double totalFee;       //现金支付金额
    private Double cashFee;        //现金支付金额
    private String timeEnd;        //支付完成时间


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getCashFee() {
        return cashFee;
    }

    public void setCashFee(Double cashFee) {
        this.cashFee = cashFee;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }


    @Override
    public String toString() {
        return "WxAffirm{" +
                "transactionId='" + transactionId + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", openid='" + openid + '\'' +
                ", isSubscribe='" + isSubscribe + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", bankType='" + bankType + '\'' +
                ", totalFee=" + totalFee +
                ", cashFee=" + cashFee +
                ", timeEnd='" + timeEnd + '\'' +
                '}';
    }
}
