package com.zbw911.tester.actuatortester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActuatortesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatortesterApplication.class, args);
    }

}
