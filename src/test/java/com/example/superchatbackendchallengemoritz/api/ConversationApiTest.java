package com.example.superchatbackendchallengemoritz.api;

import com.example.superchatbackendchallengemoritz.BaseMvcTest;
import com.example.superchatbackendchallengemoritz.api.model.Conversation;
import com.example.superchatbackendchallengemoritz.api.model.Message;
import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.ConversationRepository;
import com.example.superchatbackendchallengemoritz.persistence.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ConversationApiTest extends BaseMvcTest {

    @Autowired private ConversationRepository conversationRepository;
    @Autowired private MessageRepository messageRepository;
    @Autowired private ContactRepository contactRepository;

    @BeforeEach
    public void beforeEach() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    public void getConversations() {
        /*
         * Given
         */
        com.example.superchatbackendchallengemoritz.persistence.model.Contact recipientEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Contact();
        recipientEntity.setFirstName(UUID.randomUUID().toString());
        recipientEntity.setLastName(UUID.randomUUID().toString());
        recipientEntity.setEmail(UUID.randomUUID().toString());
        recipientEntity.setPhoneNumber(UUID.randomUUID().toString());
        recipientEntity = contactRepository.save(recipientEntity);
        com.example.superchatbackendchallengemoritz.persistence.model.Contact senderEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Contact();
        senderEntity.setFirstName(UUID.randomUUID().toString());
        senderEntity.setLastName(UUID.randomUUID().toString());
        senderEntity.setEmail(UUID.randomUUID().toString());
        senderEntity.setPhoneNumber(UUID.randomUUID().toString());
        senderEntity = contactRepository.save(senderEntity);
        String content = "Hello World! {{recipient}}";
        com.example.superchatbackendchallengemoritz.persistence.model.Message messageEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Message();
        messageEntity.setRecipient(recipientEntity);
        messageEntity.setSender(senderEntity);
        messageEntity.setContent(content);
        messageEntity = messageRepository.save(messageEntity);
        com.example.superchatbackendchallengemoritz.persistence.model.Conversation
                conversationEntity =
                        new com.example.superchatbackendchallengemoritz.persistence.model
                                .Conversation();
        conversationEntity.setRecipient(recipientEntity);
        conversationEntity.setSender(senderEntity);
        conversationEntity.setMessages(List.of(messageEntity));
        conversationEntity = conversationRepository.save(conversationEntity);
        messageEntity.setConversation(conversationEntity);
        messageEntity = messageRepository.save(messageEntity);

        /*
         * When
         */
        ResponseEntity<List<Conversation>> response =
                restTemplate.exchange(
                        "/api/v1/conversations/" + recipientEntity.getId().toString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        /*
         * Then
         */
        List<Conversation> conversations = response.getBody();
        Assertions.assertNotNull(conversations);
        Assertions.assertEquals(1, conversations.size());
        Conversation conversation = conversations.get(0);
        List<Message> messages = conversation.getMessages();
        Assertions.assertEquals(1, messages.size());
        Message message = messages.get(0);
        Assertions.assertEquals(
                String.format(
                        "Hello World! %s %s",
                        recipientEntity.getFirstName(), recipientEntity.getLastName()),
                message.getContent());
    }

    @Test
    public void createMessage() {
        /*
         * Given
         */
        com.example.superchatbackendchallengemoritz.persistence.model.Contact recipientEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Contact();
        recipientEntity.setFirstName(UUID.randomUUID().toString());
        recipientEntity.setLastName(UUID.randomUUID().toString());
        recipientEntity.setEmail(UUID.randomUUID().toString());
        recipientEntity.setPhoneNumber(UUID.randomUUID().toString());
        recipientEntity = contactRepository.save(recipientEntity);
        com.example.superchatbackendchallengemoritz.persistence.model.Contact senderEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Contact();
        senderEntity.setFirstName(UUID.randomUUID().toString());
        senderEntity.setLastName(UUID.randomUUID().toString());
        senderEntity.setEmail(UUID.randomUUID().toString());
        senderEntity.setPhoneNumber(UUID.randomUUID().toString());
        senderEntity = contactRepository.save(senderEntity);
        String content = "Hello World!";
        Message message = new Message(recipientEntity.getId(), senderEntity.getId(), content, null);

        /*
         * When
         */
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/conversations",
                        HttpMethod.POST,
                        new HttpEntity<>(message),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        /*
         * Then
         */
        List<com.example.superchatbackendchallengemoritz.persistence.model.Message> messages =
                messageRepository.findAllByRecipientAndSender(recipientEntity, senderEntity);
        Assertions.assertEquals(1, messages.size());
        com.example.superchatbackendchallengemoritz.persistence.model.Message messageEntity =
                messages.get(0);
        Assertions.assertEquals(content, messageEntity.getContent());
    }
}
