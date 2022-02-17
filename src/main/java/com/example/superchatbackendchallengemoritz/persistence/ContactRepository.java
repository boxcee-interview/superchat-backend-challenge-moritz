package com.example.superchatbackendchallengemoritz.persistence;

import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, UUID> {}
