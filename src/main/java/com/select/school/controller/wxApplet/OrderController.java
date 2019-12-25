/**
 * projectName: selectSchool
 * fileName: OrderController.java
 * packageName: com.select.school.controller.wxApplet
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.controller.wxApplet;

import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Order;
import com.select.school.service.web.OrderService;
import com.select.school.service.wxApplet.WeCharPayService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: OrderController
 * @packageName: com.select.school.controller.wxApplet
 * @description: 订单记录
 * @data: 2019-11-13
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表查询接口
     *
     * @param pagedata
     * @return
     */
        @RequestMapping(method = { RequestMethod.POST}, value = "/orderList")
    public String schoolList(@RequestBody PagedataDto pagedata) {
        String schoolList = orderService.selectAll(pagedata);
        return schoolList;
    }

    // 微信支付统一下单
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public Object wxpay(@RequestBody Order order){

        ResponseResult result = new ResponseResult();
        if (order ==null ){
            return result.setCodeMsg(ResponseCode.REQUEST_NOT);
        }
        return "";
    }

}