package com.select.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource(locations = "classpath*:/datasource.xml")
public class App {

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

        System.out.println("\t===========================================");
        System.out.println("\t==================启动成功==================");
        System.out.println("\t===========================================");
    }

}
