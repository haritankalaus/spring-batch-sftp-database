package com.example.batchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchDemoApplication.class, args);
    }
}
