/**
 * projectName: selectSchool
 * fileName: WechatConfig.java
 * packageName: com.select.school.common.config
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WechatConfig
 * @packageName: com.select.school.common.config
 * @description: 微信属性配置
 * @data: 2019-09-24
 **/

@Component
@Data
@ConfigurationProperties(prefix = "wechat")
public  class WechatConfig {

    private String appid;
    private String appscrect;
    private String key;
    private String notify_url;
    private String mch_id;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppscrect() {
        return appscrect;
    }

    public void setAppscrect(String appscrect) {
        this.appscrect = appscrect;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
}
