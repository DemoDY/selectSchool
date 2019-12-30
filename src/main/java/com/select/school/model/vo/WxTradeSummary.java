/**
 * projectName: selectSchool
 * fileName: WxTradeSummary.java
 * packageName: com.select.school.model.vo
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.vo;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WxTradeSummary
 * @packageName: com.select.school.model.vo
 * @description:
 * @data: 2019-11-14
 **/
public class WxTradeSummary {
    private Integer id; // 自动生成id
    private String billDate; // 查询日期
    private String tradeCount;// 总交易单数
    private String shouldSettleAmount;// 应结订单总金额
    private String refundAmount;// 退款总金额
    private String rechargeVoucherAmount;// 充值券退款总金额
    private String ServiceCharge;// 手续费总金额
    private String totalOrderAmount;// 订单总金额
    private String applyRefundAmount;// 申请退款总金额

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

    public String getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(String tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getShouldSettleAmount() {
        return shouldSettleAmount;
    }

    public void setShouldSettleAmount(String shouldSettleAmount) {
        this.shouldSettleAmount = shouldSettleAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRechargeVoucherAmount() {
        return rechargeVoucherAmount;
    }

    public void setRechargeVoucherAmount(String rechargeVoucherAmount) {
        this.rechargeVoucherAmount = rechargeVoucherAmount;
    }

    public String getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public String getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(String totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getApplyRefundAmount() {
        return applyRefundAmount;
    }

    public void setApplyRefundAmount(String applyRefundAmount) {
        this.applyRefundAmount = applyRefundAmount;
    }
}
