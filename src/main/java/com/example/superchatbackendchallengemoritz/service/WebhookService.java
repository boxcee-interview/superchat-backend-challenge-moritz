package com.example.superchatbackendchallengemoritz.service;

import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.WebhookRequestRepository;
import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequest;
import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequestContact;
import com.example.superchatbackendchallengemoritz.service.model.ContactDto;
import com.example.superchatbackendchallengemoritz.service.model.MessageDto;
import com.example.superchatbackendchallengemoritz.service.model.WebhookRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRequestRepository webhookRequestRepository;
    private final WebhookRequestContactRepository webhookRequestContactRepository;
    private final ContactRepository contactRepository;
    private final ConversationService conversationService;

    public void saveIncomingWebhookRequest(WebhookRequestDto webhookRequestDto) {
        WebhookRequestContact recipientEntity = toEntity(webhookRequestDto.getRecipient());
        recipientEntity = webhookRequestContactRepository.save(recipientEntity);
        WebhookRequestContact senderEntity = toEntity(webhookRequestDto.getSender());
        senderEntity = webhookRequestContactRepository.save(senderEntity);
        WebhookRequest webhookRequest = new WebhookRequest();
        webhookRequest.setRecipient(recipientEntity);
        webhookRequest.setSender(senderEntity);
        webhookRequest.setMessage(webhookRequestDto.getMessage());
        webhookRequestRepository.save(webhookRequest);
    }

    private WebhookRequestContact toEntity(ContactDto contactDto) {
        WebhookRequestContact webhookRequestContact = new WebhookRequestContact();
        webhookRequestContact.setFirstName(contactDto.getFirstName());
        webhookRequestContact.setLastName(contactDto.getLastName());
        webhookRequestContact.setEmail(contactDto.getEmail());
        webhookRequestContact.setPhoneNumber(contactDto.getPhoneNumber());
        return webhookRequestContact;
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void processWebhookRequests() {
        log.info("processWebhookRequests");
        List<WebhookRequest> webhookRequests = webhookRequestRepository.findAll();
        List<WebhookRequestContact> whrcToBeDeleted = new ArrayList<>();
        List<WebhookRequest> whrToBeDeleted =
                webhookRequests.stream()
                        .filter(
                                whr -> {
                                    Contact recipient = findContact(whr.getRecipient());
                                    if (recipient == null) {
                                        log.warn(
                                                "Could not match recipient to contact in DB. ID:"
                                                        + " {}",
                                                whr.getRecipient().getId());
                                        return false;
                                    }

                                    Contact sender = findContact(whr.getSender());
                                    if (sender == null) {
                                        log.warn(
                                                "Could not match sender to contact in DB. ID: {}",
                                                whr.getSender().getId());
                                        return false;
                                    }

                                    MessageDto messageDto =
                                            new MessageDto(
                                                    recipient.getId(),
                                                    sender.getId(),
                                                    whr.getMessage(),
                                                    null);
                                    conversationService.createMessage(messageDto);

                                    whrcToBeDeleted.add(whr.getRecipient());
                                    whrcToBeDeleted.add(whr.getSender());

                                    return true;
                                })
                        .collect(Collectors.toList());
        webhookRequestRepository.deleteAll(whrToBeDeleted);
        webhookRequestContactRepository.deleteAll(whrcToBeDeleted);
    }

    private Contact findContact(WebhookRequestContact webhookRequestContact) {
        Optional<Contact> contactOpt =
                contactRepository.findByFirstNameAndLastName(
                        webhookRequestContact.getFirstName(), webhookRequestContact.getLastName());
        if (contactOpt.isEmpty()) {
            contactOpt = contactRepository.findByEmail(webhookRequestContact.getEmail());
        }
        if (contactOpt.isEmpty()) {
            contactOpt =
                    contactRepository.findByPhoneNumber(webhookRequestContact.getPhoneNumber());
        }
        return contactOpt.orElse(null);
    }
}
