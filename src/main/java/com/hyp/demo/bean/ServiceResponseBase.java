package com.hyp.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import com.hyp.demo.consts.CommonConsts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ServiceResponseBase implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5245734143804524050L;
    /**
     * 返回的消息code，100--正常，300--异常
     */
    @ApiModelProperty(value="返回的消息code，200--正常，300--异常")
    private String responseCode = CommonConsts.RESPONSE_CODE_SUCCESS;

    /**
     * 返回的消息list
     */
    @ApiModelProperty(value="返回的消息list")
    private ArrayList<SystemMessage> messageList = new ArrayList<SystemMessage>();
    
    /**
     * 版本号
     */
    @ApiModelProperty(value="版本号")
    private String version;
    
}
