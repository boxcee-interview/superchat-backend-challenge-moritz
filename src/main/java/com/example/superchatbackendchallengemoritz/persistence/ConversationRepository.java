package com.example.superchatbackendchallengemoritz.persistence;

import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import com.example.superchatbackendchallengemoritz.persistence.model.Conversation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Optional<Conversation> findByRecipientAndSender(Contact recipient, Contact sender);

    List<Conversation> findAllByRecipientOrSender(Contact recipient, Contact sender);
}
