package com.example.superchatbackendchallengemoritz.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.superchatbackendchallengemoritz.BaseMvcTest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TemplatingServiceTest extends BaseMvcTest {

    @Autowired private TemplatingService templatingService;

    @Test
    void substitute_btcToUsd() {
        /*
         * Given
         */
        String input = "I have 20.12345678 BTC in my wallet.";

        /*
         * When
         */
        String output = templatingService.substitute(input, null);

        /*
         * Then
         */
        Assertions.assertTrue(output.matches("I have (.*) USD in my wallet\\."));
    }

    @Test
    void substitute_recipientAndSender() {
        /*
         * Given
         */
        String input = "From {{ sender }} to {{ recipient }}.";
        Map<String, String> context = new HashMap<>();
        context.put("recipient", "TO");
        context.put("sender", "FROM");

        /*
         * When
         */
        String output = templatingService.substitute(input, context);

        /*
         * Then
         */
        Assertions.assertEquals("From FROM to TO.", output);
    }
}
