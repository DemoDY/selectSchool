/**
 * projectName: selectSchool
 * fileName: WechatConfig.java
 * packageName: com.select.school.common.config
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.common.config;

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
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    private String appid;
    private String appscrect;
    private String key;

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
}
