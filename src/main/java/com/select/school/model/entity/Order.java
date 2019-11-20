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
    private String orderNumber;
    private String userId;
    private double totalFee;
    private Integer state;
    private String creatTime;
    private String openid;
    private String updateTime;

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

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
