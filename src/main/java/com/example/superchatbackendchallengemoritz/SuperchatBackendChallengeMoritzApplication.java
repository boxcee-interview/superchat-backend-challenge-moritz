package com.example.superchatbackendchallengemoritz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaAuditing
@EnableJpaRepositories
public class SuperchatBackendChallengeMoritzApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperchatBackendChallengeMoritzApplication.class, args);
    }
}
