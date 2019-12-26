/**
 * projectName: selectSchool
 * fileName: Money.java
 * packageName: com.select.school.model.entity
 * date: 2019-12-26 14:16
 * copyright(c) 德慧
 */
package com.select.school.model.entity;

public class Money {

    private int state; // 1  管理员测试用 ， 0  用户体验
    private double money;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
