/**
 * projectName: selectSchool
 * fileName: WeCharPayServiceImpl.java
 * packageName: com.select.school.service.wxApplet
 * date: 2019-11-12
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.service.wxApplet;

import com.select.school.model.vo.WxPayVo;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeCharPayServiceImpl
 * @packageName: com.select.school.service.wxApplet
 * @description: 微信支付
 * @data: 2019-11-12
 **/
public interface WeCharPayService {

    String wxPay(WxPayVo wxPayVo);

    Object affirm(HttpServletRequest request);
}
