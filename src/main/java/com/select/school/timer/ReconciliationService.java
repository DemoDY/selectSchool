/**
 * projectName: selectSchool
 * fileName: ReconciliationService.java
 * packageName: com.select.school.timer
 * date: 2019-11-14
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.timer;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: ReconciliationService
 * @packageName: com.select.school.timer
 * @description: 对账定时器
 * @data: 2019-11-14
 **/
public class ReconciliationService {


    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled(){
//        log.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
    }
}
