package com.sparta.springtrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringTrelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTrelloApplication.class, args);
    }

}
