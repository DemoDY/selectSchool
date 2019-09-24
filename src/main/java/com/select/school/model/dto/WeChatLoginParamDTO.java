/**
 * projectName: selectSchool
 * fileName: WeChatLoginParamDTO.java
 * packageName: com.select.school.vo
 * date: 2019-09-24
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.model.dto;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: WeChatLoginParamDTO
 * @packageName: com.select.school.vo
 * @description: 微信小程序登录参数
 * @data: 2019-09-24
 **/
public class WeChatLoginParamDTO {

    private String code;
    private String nickName;    //昵称
    private String avatarUrl;   //头像地址
    private String moblie;      //手机号

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    @Override
    public String toString() {
        return "WeChatLoginParamDTO{" +
                "code='" + code + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", moblie='" + moblie + '\'' +
                '}';
    }
}
