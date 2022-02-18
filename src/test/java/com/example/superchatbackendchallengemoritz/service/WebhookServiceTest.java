package com.example.superchatbackendchallengemoritz.service;

import com.example.superchatbackendchallengemoritz.BaseMvcTest;
import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.ConversationRepository;
import com.example.superchatbackendchallengemoritz.persistence.MessageRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestRepository;
import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import com.example.superchatbackendchallengemoritz.persistence.model.Message;
import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequest;
import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequestContact;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WebhookServiceTest extends BaseMvcTest {

    @Autowired private WebhookService webhookService;
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
    public void processWebhookRequests() {
        /*
         * Given
         */
        Contact recipient = new Contact();
        recipient.setFirstName(UUID.randomUUID().toString());
        recipient.setLastName(UUID.randomUUID().toString());
        recipient.setEmail(UUID.randomUUID().toString());
        recipient.setPhoneNumber(UUID.randomUUID().toString());
        recipient = contactRepository.save(recipient);
        Contact sender = new Contact();
        sender.setFirstName(UUID.randomUUID().toString());
        sender.setLastName(UUID.randomUUID().toString());
        sender.setEmail(UUID.randomUUID().toString());
        sender.setPhoneNumber(UUID.randomUUID().toString());
        sender = contactRepository.save(sender);
        WebhookRequestContact recipientContact = new WebhookRequestContact();
        recipientContact.setFirstName(UUID.randomUUID().toString());
        recipientContact.setLastName(UUID.randomUUID().toString());
        recipientContact.setEmail(recipient.getEmail());
        recipientContact.setPhoneNumber(UUID.randomUUID().toString());
        recipientContact = webhookRequestContactRepository.save(recipientContact);
        WebhookRequestContact senderContact = new WebhookRequestContact();
        senderContact.setFirstName(UUID.randomUUID().toString());
        senderContact.setLastName(UUID.randomUUID().toString());
        senderContact.setEmail(sender.getEmail());
        senderContact.setPhoneNumber(UUID.randomUUID().toString());
        senderContact = webhookRequestContactRepository.save(senderContact);
        WebhookRequest webhookRequest = new WebhookRequest();
        webhookRequest.setRecipient(recipientContact);
        webhookRequest.setSender(senderContact);
        webhookRequest.setMessage("Hello World!");
        webhookRequest = webhookRequestRepository.save(webhookRequest);

        /*
         * When
         */
        webhookService.processWebhookRequests();

        /*
         * Then
         */
        List<Message> messages = messageRepository.findAll();
        Assertions.assertEquals(1, messages.size());
        Message message = messages.get(0);
        Assertions.assertEquals("Hello World!", message.getContent());
    }
}
