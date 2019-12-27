/**
 * projectName: selectSchool
 * fileName: OrderImpl.java
 * packageName: com.select.school.service.web.impl
 * date: 2019-12-23 15:59
 * copyright(c) 德慧
 */
package com.select.school.service.web.impl;

import com.select.school.mapper.OrderMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Order;
import com.select.school.service.web.OrderService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String selectAll(PagedataDto pagedata) {
        ResponseResult result = new ResponseResult();
        PagedataDto pagedataDto = new PagedataDto();
        List<Order> orderList = orderMapper.selectAll(SqlParameter.getParameter()
                .addLimit(pagedata.getPageNum(), pagedata.getPageSize())
                .addQuery("orderNumber",pagedata.getOrderNumber())
                .addQuery("userId",pagedata.getUserId())
                .addQuery("start",pagedata.getStartTime())
                .addQuery("end",pagedata.getEndTime())
                .getMap());
        int count = orderMapper.count(SqlParameter.getParameter()
                .addQuery("orderNumber",pagedata.getOrderNumber())
                .addQuery("userId",pagedata.getUserId())
                .addQuery("start",pagedata.getStartTime())
                .addQuery("end",pagedata.getEndTime()).getMap());
        if (orderList == null || orderList.size() == 0) {
            result.setCodeMsg(ResponseCode.QUERY_NO_DATAS);
        } else {
            ArrayList<Order> list = new ArrayList<>();
            list.addAll(orderList);
            pagedataDto.setRecords(orderList);
            pagedataDto.setPageNum(pagedata.getPageNum());
            pagedataDto.setPageSize(pagedata.getPageSize());
            pagedataDto.setPages((int)Math.ceil(count/pagedata.getPageSize()+1));
            pagedataDto.setTotal(count);
            result.setCodeMsg(ResponseCode.SUCCESS);
            result.setData(pagedataDto);
        }
        return JSONObject.fromObject(result).toString();
    }
}

