package com.example.superchatbackendchallengemoritz.api;

import static org.junit.jupiter.api.Assertions.*;

import com.example.superchatbackendchallengemoritz.BaseMvcTest;
import com.example.superchatbackendchallengemoritz.api.model.Contact;
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

class ContactApiTest extends BaseMvcTest {

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
    public void getContacts() {
        /*
         * Given
         */
        com.example.superchatbackendchallengemoritz.persistence.model.Contact contactEntity =
                new com.example.superchatbackendchallengemoritz.persistence.model.Contact();
        contactEntity.setFirstName(UUID.randomUUID().toString());
        contactEntity.setLastName(UUID.randomUUID().toString());
        contactEntity.setEmail(UUID.randomUUID().toString());
        contactEntity.setPhoneNumber(UUID.randomUUID().toString());
        contactEntity = contactRepository.save(contactEntity);

        /*
         * When
         */
        ResponseEntity<List<Contact>> response =
                restTemplate.exchange(
                        "/api/v1/contacts",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        /*
         * Then
         */
        List<Contact> contacts = response.getBody();
        Assertions.assertNotNull(contacts);
        Assertions.assertEquals(1, contacts.size());
        Contact contact = contacts.get(0);
        Assertions.assertEquals(contactEntity.getFirstName(), contact.getFirstName());
        Assertions.assertEquals(contactEntity.getLastName(), contact.getLastName());
        Assertions.assertEquals(contactEntity.getEmail(), contact.getEmail());
        Assertions.assertEquals(contactEntity.getPhoneNumber(), contact.getPhoneNumber());
    }

    @Test
    public void createContact() {
        /*
         * Given
         */
        Contact contact =
                new Contact(
                        null,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString());

        /*
         * When
         */
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/contacts", HttpMethod.POST, new HttpEntity<>(contact), Void.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        /*
         * Then
         */
        List<com.example.superchatbackendchallengemoritz.persistence.model.Contact> contacts =
                contactRepository.findAll();
        Assertions.assertEquals(1, contacts.size());
        com.example.superchatbackendchallengemoritz.persistence.model.Contact contactEntity =
                contacts.get(0);
        Assertions.assertEquals(contact.getFirstName(), contactEntity.getFirstName());
        Assertions.assertEquals(contact.getLastName(), contactEntity.getLastName());
        Assertions.assertEquals(contact.getEmail(), contactEntity.getEmail());
        Assertions.assertEquals(contact.getPhoneNumber(), contactEntity.getPhoneNumber());
    }
}
