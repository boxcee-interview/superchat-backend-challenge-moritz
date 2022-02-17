package com.example.superchatbackendchallengemoritz.service;

import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import com.example.superchatbackendchallengemoritz.service.model.ContactDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<ContactDto> getContacts() {
        return contactRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void createContact(ContactDto contact) {
        contactRepository.save(toEntity(contact));
    }

    private Contact toEntity(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhoneNumber(contactDto.getPhoneNumber());
        return contact;
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
