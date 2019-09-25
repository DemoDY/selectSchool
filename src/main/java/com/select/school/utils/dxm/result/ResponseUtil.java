/**
 * projectName: selectSchool
 * fileName: ResponseUtil.java
 * packageName: com.select.school.utils.dxm.result
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.utils.dxm.result;

import net.sf.json.JSONObject;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: ResponseUtil
 * @packageName: com.select.school.utils.dxm.result
 * @description: 返回数据包装类
 * @data: 2019-09-24
 **/
public class ResponseUtil {

    /**
     * 返回不包含data参数
     * @param responseCode
     * @return
     */
    public static String setResult(ResponseCode responseCode){
        ResponseResult response = new ResponseResult();
        return JSONObject.fromObject(response.setCodeMsg(responseCode)).toString();
    }

    /**
     * 返回包含data参数
     * @param responseCode
     * @return
     */
    public static String setResult(ResponseCode responseCode,Object data){
        ResponseResult response = new ResponseResult();
        response.setCodeMsg(responseCode);
        response.setData(JSONObject.fromObject(data));
        return JSONObject.fromObject(response).toString();
    }

}
