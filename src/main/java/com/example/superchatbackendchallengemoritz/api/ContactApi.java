package com.example.superchatbackendchallengemoritz.api;

import com.example.superchatbackendchallengemoritz.api.model.Contact;
import com.example.superchatbackendchallengemoritz.service.ContactService;
import com.example.superchatbackendchallengemoritz.service.model.ContactDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/contacts")
@RequiredArgsConstructor
public class ContactApi {

    private final ContactService contactService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getContacts() {
        List<ContactDto> contactDtos = contactService.getContacts();
        return new ResponseEntity<>(toApi(contactDtos), HttpStatus.OK);
    }

    private List<Contact> toApi(List<ContactDto> contactDtos) {
        return contactDtos.stream().map(this::toApi).collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createContact(@RequestBody Contact contact) {
        contactService.createContact(toDto(contact));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private ContactDto toDto(Contact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhoneNumber());
    }

    private Contact toApi(ContactDto contactDto) {
        return new Contact(
                contactDto.getId(),
                contactDto.getFirstName(),
                contactDto.getLastName(),
                contactDto.getEmail(),
                contactDto.getPhoneNumber());
    }
}
