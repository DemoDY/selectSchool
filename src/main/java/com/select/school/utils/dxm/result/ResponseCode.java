package com.select.school.utils.dxm.result;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: ResponseCode
 * @packageName: com.fc.aden.util.dxm.result
 * @description: CODE MSG枚举
 * @data: 2019-09-24
 **/
public enum ResponseCode {

    NOT_AUTH(102, "无权限"),
    REQUEST_NOT(411, "参数错误"),
    LOGIN_TIMEOUT(101, "登录超时"),
    SUCCESS(200, "操作成功"),
    OPERATE_ERROR(300, "操作不合法"),
    OPERATE_NOT_REPEAT_DATA(301, "不能重复提交数据"),
    OPERATE_DATA_TO_LANG(302, "数据长度超限, 无法继续新增数据"),
    SEND_SMS_VALIDATE_ERROR(40, "短信验证码错误"),
    CODE_VALIDATE_ERROR(41, "验证码错误"),
    OLD_PWD_ERROR(205, "原密码不正确"),
    ACCOUNT_DISABLE(205, "账号已停用"),
    USERNAME_PASSWORD_ERROR(207, "用户名或密码错误"),
    ACCOUNT_ALREADY(205, "账户已经存在"),
    QUERY_NO_DATAS(208, "未查询到数据"),
    ERROR(500, "服务器出错"),
    ERROR_SERVICE_NOT_EXIST(501, "服务不存在"),
    ACCOUNT_NOT(501, "账户不存在"),
    PASSWORD_NOT(502, "密码不为空"),
    PASSWORD_ERROR(201, "密码不正确");


    private Integer code;
    private String value;
    private static Map<Integer, ResponseCode> MAPPING = new HashMap();

    private ResponseCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getText() {
        return this.value;
    }

    public Integer getCode() {
        return this.code;
    }

    public static ResponseCode forCode(int code) {
        return (ResponseCode)MAPPING.get(code);
    }

    public static ResponseCode forText(String text) {
        ResponseCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResponseCode model = var1[var3];
            if (model.getText().equals(text)) {
                return model;
            }
        }

        return null;
    }

    static {
        ResponseCode[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            ResponseCode model = var0[var2];
            MAPPING.put(model.getCode(), model);
        }

    }
}
