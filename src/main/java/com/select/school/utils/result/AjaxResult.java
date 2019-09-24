package com.select.school.utils.result;

import java.util.HashMap;

/**
 * @author fuce
 * @ClassName: AjaxResult
 * @Description: TODO(ajax操作消息提醒)
 * @date 2018年8月18日
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    /**
     * 初始化一个新创建的 Message 对象
     */
    public AjaxResult() {
    }

    public static final Integer CODE_ERROR = 1;
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_SERVER_ERROR = 500;
    public static final String AJAX_DATA = "data";
    public static final String AJAX_CODE = "code";
    public static final String AJAX_MSG = "msg";
    public static final String AJAX_TOKEN = "token";

    public static final String AJAX_KEY = "key";
    public static final String CODE_ERROR_MSG = "操作失败";

    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static AjaxResult error() {
        return error(CODE_ERROR, CODE_ERROR_MSG);
    }

    /**
     * 返回错误消息
     *
     * @param msg 内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg) {
        return error(CODE_SERVER_ERROR, msg);
    }

    /**
     * 返回错误消息
     *
     * @param code 错误码
     * @param msg  内容
     * @return 错误消息
     */
    public static AjaxResult error(int code, String msg) {
        AjaxResult json = new AjaxResult();
        json.put(AJAX_CODE, code);
        json.put(AJAX_MSG, msg);
        return json;
    }

    /**
     * @return com.fc.aden.common.domain.AjaxResult
     * @Author Noctis
     * @Description //返回成功消息
     * @Date 2019/6/24 16:42
     * @Param [code, msg]
     **/
    public static AjaxResult success(int code, String msg) {
        AjaxResult json = new AjaxResult();
        json.put(AJAX_CODE, code);
        json.put(AJAX_MSG, msg);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @param msg 内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        AjaxResult json = new AjaxResult();
        json.put(AJAX_MSG, msg);
        json.put(AJAX_CODE, 200);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    public static AjaxResult successData(int key, Object value) {
        AjaxResult json = new AjaxResult();
        json.put(AJAX_KEY, key);
        json.put(AJAX_DATA, value);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @param key   键值
     * @param value 内容
     * @return 成功消息
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
