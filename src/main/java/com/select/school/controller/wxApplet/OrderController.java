/**
 * projectName: selectSchool
 * fileName: OrderController.java
 * packageName: com.select.school.controller.wxApplet
 * date: 2019-11-13
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.controller.wxApplet;

import com.select.school.mapper.OrderMapper;
import com.select.school.model.dto.PagedataDto;
import com.select.school.model.entity.Order;
import com.select.school.service.web.OrderService;
import com.select.school.service.wxApplet.WeCharPayService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.dxm.wechat.WeChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Resource
    private OrderMapper orderMapper;

    /**
     * 订单列表查询接口
     *
     * @param pagedata
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/orderList")
    public String schoolList(@RequestBody PagedataDto pagedata) {
        String schoolList = orderService.selectAll(pagedata);
        return schoolList;
    }

    /**
     * @param order
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public void wxpay(@RequestBody Order order) {
        try {
            String yesterday = "2019-12-17";
            String today = "2019-12-18";
            // 获取昨日、已支付、已完成、未对账的订单
            List<Order> orderList = orderMapper.selectAll(SqlParameter.getParameter()
                    .addQuery("stateList", "stateList")
                    .addQuery("start", yesterday)
                    .addQuery("end", today)
                    .addQuery("checkState", 0)
                    .getMap());
            List<Order> orderListChark = orderMapper.selectAll(SqlParameter.getParameter()
                    .addQuery("stateList", "stateList")
                    .addQuery("start", yesterday)
                    .addQuery("end", today)
                    .addQuery("checkState", 1)
                    .getMap());
            if (orderList.size() > 0) {
                List<String> list1 = toStringList(orderList);
                Map<String, List<String>> contrast = WeChatUtils.contrast(list1);
                List<String> successOrderList = contrast.get("successOrderList");
                if (contrast.get("successOrderList").size() > 0) {
                    orderMapper.update(SqlParameter.getParameter()
                            .addUpdate("state", 4)
                            .addUpdate("checkState", 1)
                            .addQuery("orderList", successOrderList)
                            .getMap());
                }
                if (contrast.get("errorOrderList").size() > 0) {
                    orderMapper.update(SqlParameter.getParameter()
                            .addUpdate("checkState", 1)
                            .addQuery("orderList", contrast.get("errorOrderList"))
                            .getMap());
                }

            }
            if (orderListChark.size() > 0) {
                List<String> list2 = toStringList(orderListChark);
                Map<String, List<String>> contrastChark = WeChatUtils.contrast(list2);
                if (contrastChark != null && contrastChark.get("successOrderList").size() > 0) {
                    orderMapper.update(SqlParameter.getParameter()
                            .addUpdate("state", 4)
                            .addUpdate("checkState", 1)
                            .addQuery("orderList", contrastChark.get("successOrderList"))
                            .getMap());
                }
                if (contrastChark != null && contrastChark.get("errorOrderList").size() > 0) {
                    orderMapper.update(SqlParameter.getParameter()
                            .addUpdate("state", 6)
                            .addUpdate("checkState", 2)
                            .addQuery("orderList", contrastChark.get("errorOrderList"))
                            .getMap());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> toStringList(List<Order> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Order order : list) {
            arrayList.add(order.getOrderNumber() + "%" + order.getOpenid() + order.getTotalFee());
        }
        return arrayList;
    }
}