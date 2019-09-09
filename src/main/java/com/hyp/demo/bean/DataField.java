package com.hyp.demo.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class DataField implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5175444299033575942L;
    
    private String key;
    
    private Object value;
}
