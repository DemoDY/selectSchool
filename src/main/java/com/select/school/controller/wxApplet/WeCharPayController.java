/**
 * projectName: selectSchool
 * fileName: WeCharPayController.java
 * packageName: com.select.school.controller.wxApplet
 * date: 2019-11-12
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.controller.wxApplet;

import com.select.school.model.vo.WxPayVo;
import com.select.school.service.wxApplet.WeCharPayService;
import com.select.school.utils.dxm.result.ResponseCode;
import com.select.school.utils.dxm.result.ResponseResult;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeCharPayController
 * @packageName: com.select.school.controller.wxApplet
 * @description: 微信支付
 * @data: 2019-11-12
 **/

@RestController
@RequestMapping(value = "/wx")
public class WeCharPayController {

@Resource
private WeCharPayService weCharPayService;


    /**
     * 微信支付统一下单
     * @param wxPayVo
     * @return Object
     */

    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    public Object wxpay(@RequestBody WxPayVo wxPayVo){

        ResponseResult result = new ResponseResult();
        if (wxPayVo ==null || wxPayVo.getTotalFee() == 0 || wxPayVo.getTradeType() == null){
            return result.setCodeMsg(ResponseCode.REQUEST_NOT);
        }
        return weCharPayService.wxPay(wxPayVo);
    }

    /**
     * 微信支付统一下单
     * @param request
     * @return Object
     */

    @RequestMapping(value = "/feedback", method = {RequestMethod.POST,RequestMethod.GET})
    public Object affirm(HttpRequest request){

        return weCharPayService.affirm(request);
    }



}
