package com.select.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;


@ImportResource(locations = "classpath*:/datasource.xml")
@EnableScheduling
@SpringBootApplication
public class App {

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

        System.out.println("\t===========================================");
        System.out.println("\t==================启动成功==================");
        System.out.println("\t===========================================");
    }

}
