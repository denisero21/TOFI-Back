package com.example.tofi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TofiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TofiApplication.class, args);
    }

}
