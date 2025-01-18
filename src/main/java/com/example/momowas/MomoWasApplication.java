package com.example.momowas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MomoWasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomoWasApplication.class, args);
    }

}
