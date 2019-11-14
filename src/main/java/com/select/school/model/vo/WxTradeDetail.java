/**
 * projectName: selectSchool
 * fileName: WxTradeDetail.java
 * packageName: com.select.school.model.vo
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.vo;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WxTradeDetail
 * @packageName: com.select.school.model.vo
 * @description:
 * @data: 2019-11-14
 **/
public class WxTradeDetail {
    private Integer id; // 自动生成id
    private String billDate; // 查询日期
    private String transDate;// 交易时间
    private String commonId;// 公众账号ID
    private String businessNo;// 商户号
    private String childBusinessNo;// 子商户号
    private String equipmentNo;// 设备号
    private String wxOrderNo;// 微信订单号
    private String businessOrderNo;// 商户订单号
    private String userIdentity;// 用户标识
    private String transType;// 交易类型
    private String transStatus;// 交易状态
    private String paymentBank;// 付款银行
    private String currency;// 货币种类
    private String settleAccounts;// 应结订单金额
    private String coupon;// 代金券金额
    private String businessName;// 商品名称
    private String businessData;// 商户数据包
    private String fee;// 手续费
    private String rate;// 费率
    private String rateRemark;// 费率备注
    private String orderAmount;// 订单金额

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getCommonId() {
        return commonId;
    }

    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getChildBusinessNo() {
        return childBusinessNo;
    }

    public void setChildBusinessNo(String childBusinessNo) {
        this.childBusinessNo = childBusinessNo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getWxOrderNo() {
        return wxOrderNo;
    }

    public void setWxOrderNo(String wxOrderNo) {
        this.wxOrderNo = wxOrderNo;
    }

    public String getBusinessOrderNo() {
        return businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getPaymentBank() {
        return paymentBank;
    }

    public void setPaymentBank(String paymentBank) {
        this.paymentBank = paymentBank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(String settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessData() {
        return businessData;
    }

    public void setBusinessData(String businessData) {
        this.businessData = businessData;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateRemark() {
        return rateRemark;
    }

    public void setRateRemark(String rateRemark) {
        this.rateRemark = rateRemark;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
}
