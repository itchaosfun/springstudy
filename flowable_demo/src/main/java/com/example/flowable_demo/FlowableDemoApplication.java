package com.example.flowable_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlowableDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableDemoApplication.class, args);
    }

}
