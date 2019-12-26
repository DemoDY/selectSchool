/**
 * projectName: selectSchool
 * fileName: ReconciliationService.java
 * packageName: com.select.school.timer
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.timer;

import com.select.school.utils.dxm.wechat.WeChatUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时器
 */

@Component
@EnableScheduling
public class ReconciliationService {

//*/5 * * * * ?  每五秒执行一次
//    @Scheduled(cron = "*/5 * * * * ?")
    public void scheduled(){
        try {
            Boolean b = WeChatUtils.downloadFile();
        }catch (Exception e){
            e.printStackTrace();
        }
//        String strTime = new SimpleDateFormat("HH-mm-ss").format(new Date());
//        System.out.println("测试" + strTime);
    }
}
