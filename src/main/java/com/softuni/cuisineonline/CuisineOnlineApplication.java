package com.softuni.cuisineonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CuisineOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuisineOnlineApplication.class, args);
    }

}
