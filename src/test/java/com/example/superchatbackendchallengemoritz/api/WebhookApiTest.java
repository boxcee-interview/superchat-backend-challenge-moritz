package com.example.superchatbackendchallengemoritz.api;

import com.example.superchatbackendchallengemoritz.BaseMvcTest;
import com.example.superchatbackendchallengemoritz.api.model.Contact;
import com.example.superchatbackendchallengemoritz.api.model.WebhookRequest;
import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.ConversationRepository;
import com.example.superchatbackendchallengemoritz.persistence.MessageRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestRepository;
import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequestContact;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class WebhookApiTest extends BaseMvcTest {

    @Autowired private WebhookRequestRepository webhookRequestRepository;
    @Autowired private WebhookRequestContactRepository webhookRequestContactRepository;
    @Autowired private ConversationRepository conversationRepository;
    @Autowired private MessageRepository messageRepository;
    @Autowired private ContactRepository contactRepository;

    @BeforeEach
    public void beforeEach() {
        webhookRequestRepository.deleteAll();
        webhookRequestContactRepository.deleteAll();
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    public void incomingWebhooks() {
        /*
         * Given
         */
        Contact recipient =
                new Contact(
                        null,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString());
        Contact sender =
                new Contact(
                        null,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString());
        WebhookRequest webhookRequest = new WebhookRequest(recipient, sender, "Hello World!");

        /*
         * When
         */
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/webhooks",
                        HttpMethod.POST,
                        new HttpEntity<>(webhookRequest),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        /*
         * Then
         */
        List<WebhookRequestContact> webhookRequestContacts =
                webhookRequestContactRepository.findAll();
        Assertions.assertEquals(2, webhookRequestContacts.size());
        Assertions.assertEquals(
                1,
                (int)
                        webhookRequestContacts.stream()
                                .filter(whrc -> whrc.getEmail().equals(recipient.getEmail()))
                                .count());
        Assertions.assertEquals(
                1,
                (int)
                        webhookRequestContacts.stream()
                                .filter(whrc -> whrc.getEmail().equals(sender.getEmail()))
                                .count());
        List<com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequest>
                webhookRequests = webhookRequestRepository.findAll();
        Assertions.assertEquals(1, webhookRequests.size());
        com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequest
                webhookRequestToTest = webhookRequests.get(0);
        Assertions.assertEquals("Hello World!", webhookRequestToTest.getMessage());
    }
}
