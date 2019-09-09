package com.hyp.demo.blogic.restDto;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyp.demo.bean.PageControllerInfo;



public class DemoResp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3631604453770834056L;
    
    private ArrayList<DemoRespS01> mybatisDemoList = null;
    
    /**
     * 分页DTO
     */
    private PageControllerInfo pageControllerInfo;

    /**  
    * 获取mybatisDemoList  
    * @return mybatisDemoList mybatisDemoList  
    */
    public ArrayList<DemoRespS01> getMybatisDemoList() {
        return mybatisDemoList;
    }
    

    /**  
    * 设置mybatisDemoList  
    * @param mybatisDemoList mybatisDemoList  
    */
    public void setMybatisDemoList(ArrayList<DemoRespS01> mybatisDemoList) {
        this.mybatisDemoList = mybatisDemoList;
    }
    

    /**  
    * 获取分页DTO  
    * @return pageControllerInfo 分页DTO  
    */
    public PageControllerInfo getPageControllerInfo() {
        return pageControllerInfo;
    }
    

    /**  
    * 设置分页DTO  
    * @param pageControllerInfo 分页DTO  
    */
    public void setPageControllerInfo(PageControllerInfo pageControllerInfo) {
        this.pageControllerInfo = pageControllerInfo;
    }
    
}
