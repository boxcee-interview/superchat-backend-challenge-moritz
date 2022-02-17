package com.example.superchatbackendchallengemoritz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class SuperchatBackendChallengeMoritzApplicationTests extends BaseMvcTest {

    @Autowired private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(applicationContext);
    }
}
