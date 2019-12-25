/**
 * projectName: selectSchool
 * fileName: PagedataDto.java
 * packageName: com.select.school.model.dto
 * date: 2019-10-20
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.dto;


import com.select.school.model.entity.User;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: PagedataDto
 * @packageName: com.select.school.model.dto
 * @description: 分页
 * @data: 2019-10-20
 **/
public class PagedataDto {
    private Integer pageNum;    //第几页
    private Integer pageSize;   //数量
    private Integer pages;      //总页数
    private Integer total;      //总数据数
    private Object records; //数据
    private String orderNumber;//订单号
    private String userId;//用户id
    private  String title;//学校名称，问题名称
    private String startTime;
    private String endTime;

    @Override
    public String toString() {
        return "PagedataDto{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pages=" + pages +
                ", total=" + total +
                ", records=" + records +
                ", orderNumber=" + orderNumber +
                ", userId=" + userId +
                ", title=" + title +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getRecords() {
        return records;
    }

    public void setRecords(Object records) {
        this.records = records;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
