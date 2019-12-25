/**
 * projectName: selectSchool
 * fileName: ReconciliationService.java
 * packageName: com.select.school.timer
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时器
 */
public class ReconciliationService {

//*/5 * * * * ?  每五秒执行一次
//    @Scheduled(cron = "*/5 * * * * ?")
    public void scheduled(){
        String strTime = new SimpleDateFormat("HH-mm-ss").format(new Date());
        System.out.println("测试" + strTime);
    }
}
