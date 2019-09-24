package com.select.school.utils;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.select.school.common.bean.PageControllerInfo;
import com.select.school.common.bean.SystemMessage;
import com.select.school.common.consts.CommonConsts;

public class Pub {

    public static void addSystemMessage(String key, String message, ServiceResponseBase responseDto) {
        ArrayList<SystemMessage> systemMessages = responseDto.getMessageList();
        if (systemMessages == null) {
            systemMessages = new ArrayList<SystemMessage>();
        }
        SystemMessage systemMessage = new SystemMessage(key, message);
        systemMessages.add(systemMessage);
        responseDto.setMessageList(systemMessages);
        responseDto.setResponseCode(CommonConsts.RESPONSE_CODE_WARNING);
    }
    
    public static void addSystemMessage(SystemMessage message, ServiceResponseBase responseDto) {
        ArrayList<SystemMessage> systemMessages = responseDto.getMessageList();
        if (systemMessages == null) {
            systemMessages = new ArrayList<SystemMessage>();
        }
        systemMessages.add(message);
        responseDto.setMessageList(systemMessages);
        responseDto.setResponseCode(CommonConsts.RESPONSE_CODE_WARNING);
    }
    
    public static <E> PageControllerInfo getPageControllerInfo(List<E> list) throws Exception {
        if (list == null) {
            throw new Exception("getPageControllerInfo方法的参数不能为空！");
        }
        if (!(list instanceof Page<?>)) {
            return null;
        }
        
        Page<E> page = (Page<E>) list;
        PageControllerInfo pageControllerInfo = new PageControllerInfo();
        pageControllerInfo.setPageNum(page.getPageNum());
        pageControllerInfo.setPageSize(page.getPageSize());
        pageControllerInfo.setTotalRecordCount(page.getTotal());
        pageControllerInfo.setTotalPages(page.getPages());
        return pageControllerInfo;
    }
}
