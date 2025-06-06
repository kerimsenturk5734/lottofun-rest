package com.lottofun.lottofunrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class LottofunRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottofunRestApplication.class, args);
    }

}
