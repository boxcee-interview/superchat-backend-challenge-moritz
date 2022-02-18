package com.example.superchatbackendchallengemoritz.persistence;

import com.example.superchatbackendchallengemoritz.persistence.model.WebhookRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRequestRepository extends JpaRepository<WebhookRequest, UUID> {}
