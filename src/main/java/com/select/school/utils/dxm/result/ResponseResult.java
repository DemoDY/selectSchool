/**
 * projectName: labelDatabase
 * fileName: ResponseResult.java
 * packageName: com.fc.aden.util.dxm.result
 * date: 2019-09-22
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.utils.dxm.result;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: ResponseResult
 * @packageName: com.fc.aden.util.dxm.result
 * @description: 返回实体对象
 * @data: 2019-09-24
 **/
public class ResponseResult {

    private int code;
    private String msg;
    private Object data;



    public ResponseResult setCodeMsg(ResponseCode rc) {
        this.code = rc.getCode();
        this.msg = rc.getText();
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
