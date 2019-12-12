/**
 * projectName: selectSchool
 * fileName: OrderController.java
 * packageName: com.select.school.controller.wxApplet
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.controller.wxApplet;

import com.select.school.model.entity.Order;
import com.select.school.service.wxApplet.WeCharPayService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: OrderController
 * @packageName: com.select.school.controller.wxApplet
 * @description: 订单记录
 * @data: 2019-11-13
 **/

@RestController
@RequestMapping(value = "/Order")
public class OrderController {

    @Resource
    private WeCharPayService weCharPayService;


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