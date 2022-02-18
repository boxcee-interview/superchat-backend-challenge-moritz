package com.example.superchatbackendchallengemoritz.persistence;

import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequestContact;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRequestContactRepository
        extends JpaRepository<WebhookRequestContact, UUID> {}
