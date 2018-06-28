package com.myfitness.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatserverApplication.class, args);
    }
}
