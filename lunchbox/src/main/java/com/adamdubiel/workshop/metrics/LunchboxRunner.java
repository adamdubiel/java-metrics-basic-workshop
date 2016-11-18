package com.adamdubiel.workshop.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LunchboxRunner {

    public static void main(String[] args) {
        SpringApplication.run(LunchboxRunner.class, args);
    }

}
