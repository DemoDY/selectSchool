/**
 * projectName: selectSchool
 * fileName: Order.java
 * packageName: com.select.school.model.entity
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.entity;

import java.io.Serializable;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: Order
 * @packageName: com.select.school.model.entity
 * @description: 订单
 * @data: 2019-11-13
 **/
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String orderNumber;//订单编号
    private String userId;//用户id
    private double totalFee;//金额
    private Integer state;//状态 1:待支付，2：已支付，3：已完成，4：已对账，5：已取消，6：未到账(微信未到账，本地有记录)
    private String createTime;//
    private String openid;
    private String updateTime;//
    private Integer checkState;//

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
