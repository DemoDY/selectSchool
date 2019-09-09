package com.hyp.demo.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 描述:sql查询返回公共基类
 * @author: hyp
 * @date: 2018年7月22日21:40:03
 * @version:v0.1
 */
@Data
public class SqlOutputBaseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6012929095434378823L;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新用户
     */
    private String lastModifiedBy;

    /**
     * 更新时间
     */
    private Date lastDate;
}