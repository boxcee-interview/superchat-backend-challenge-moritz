package com.example.superchatbackendchallengemoritz.persistence;

import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Optional<Contact> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByPhoneNumber(String phoneNumber);
}
