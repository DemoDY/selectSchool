package com.select.school.bean;

public class Const {
    public enum CodeEnum{
        success(200,"操作成功"),
        error(300,"操作失败"),
        noExistent(301,"账号不存在"),
        ban(302,"账号被禁用"),
        noRegist(305,"用户未注册"),
        wrongPassword(306,"密码输入错误"),
        noObject(402,"对象不存在"),
        badSQL(418,"SQL语句执行失败"),
        wrongServer(500,"服务器内部错误"),
        noToken(503,"无效的token"),
        wrongParam(504,"参数错误");


        CodeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static CodeEnum codeOf(int code) {
            for (CodeEnum codeEnum : values()) {
                if (codeEnum.getCode() == code) {
                    return codeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
}
