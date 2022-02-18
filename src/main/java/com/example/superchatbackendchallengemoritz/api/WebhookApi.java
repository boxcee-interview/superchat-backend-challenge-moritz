package com.example.superchatbackendchallengemoritz.api;

import com.example.superchatbackendchallengemoritz.api.model.Contact;
import com.example.superchatbackendchallengemoritz.api.model.WebhookRequest;
import com.example.superchatbackendchallengemoritz.service.WebhookService;
import com.example.superchatbackendchallengemoritz.service.model.ContactDto;
import com.example.superchatbackendchallengemoritz.service.model.WebhookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookApi {

    private final WebhookService webhookService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> incomingWebhook(@RequestBody WebhookRequest webhookRequest) {
        webhookService.saveIncomingWebhookRequest(toDto(webhookRequest));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private WebhookRequestDto toDto(WebhookRequest webhookRequest) {
        return new WebhookRequestDto(
                toDto(webhookRequest.getRecipient()),
                toDto(webhookRequest.getSender()),
                webhookRequest.getMessage());
    }

    private ContactDto toDto(Contact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhoneNumber());
    }
}
