/**
 * projectName: selectSchool
 * fileName: ReconciliationService.java
 * packageName: com.select.school.timer
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.timer;

import com.select.school.mapper.OrderMapper;
import com.select.school.model.entity.Order;
import com.select.school.utils.DateUtil;
import com.select.school.utils.dxm.sqlUtils.SqlParameter;
import com.select.school.utils.dxm.wechat.WeChatUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时器
 */

@Component
public class ReconciliationService {
    @Resource
    private OrderMapper orderMapper;

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 10 * * ?")
    public void scheduled(){
            String yesterday = DateUtil.getYesterday("yyyyMMdd");
            String today = DateUtil.getToDay("yyyyMMdd");
        try {
//            String yesterday = "2019-12-17";
//            String today = "2019-12-18";
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


    public List<String> toStringList( List<Order> list){
        ArrayList<String> arrayList = new ArrayList<>();
        for (Order order : list) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String totalFee = df.format(order.getTotalFee());
            arrayList.add(order.getOrderNumber() + "%" + order.getOpenid() + totalFee);
        }
        return arrayList;
    }
}
