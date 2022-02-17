package com.example.superchatbackendchallengemoritz.service;

import com.example.superchatbackendchallengemoritz.persistence.ContactRepository;
import com.example.superchatbackendchallengemoritz.persistence.ConversationRepository;
import com.example.superchatbackendchallengemoritz.persistence.MessageRepository;
import com.example.superchatbackendchallengemoritz.persistence.model.Contact;
import com.example.superchatbackendchallengemoritz.persistence.model.Conversation;
import com.example.superchatbackendchallengemoritz.persistence.model.Message;
import com.example.superchatbackendchallengemoritz.service.model.ContactDto;
import com.example.superchatbackendchallengemoritz.service.model.ConversationDto;
import com.example.superchatbackendchallengemoritz.service.model.MessageDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private final TemplatingService templatingService;

    public List<ConversationDto> getConversations(UUID contactId) {
        Contact contact = contactRepository.getById(contactId);
        List<Conversation> conversations =
                conversationRepository.findAllByRecipientOrSender(contact, contact);
        return conversations.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void createMessage(MessageDto messageDto) {
        UUID recipientId = messageDto.getRecipientId();
        Optional<Contact> recipientOpt = contactRepository.findById(recipientId);
        if (recipientOpt.isEmpty()) {
            throw new EntityNotFoundException("Recipient not found, id: " + recipientId);
        }
        Contact recipient = recipientOpt.get();

        UUID senderId = messageDto.getSenderId();
        Optional<Contact> senderOpt = contactRepository.findById(senderId);
        if (senderOpt.isEmpty()) {
            throw new EntityNotFoundException("Sender not found, id: " + senderId);
        }
        Contact sender = senderOpt.get();

        Message message = toEntity(messageDto);
        message.setRecipient(recipient);
        message.setSender(sender);

        Optional<Conversation> conversationOpt =
                conversationRepository.findByRecipientAndSender(recipient, sender);
        Conversation conversation;
        if (conversationOpt.isEmpty()) {
            conversation = new Conversation();
            conversation.setRecipient(recipient);
            conversation.setSender(sender);
            conversation.setMessages(List.of(message));
        } else {
            // TODO: This doesn't work for conversations with multiple participants.
            conversation = conversationOpt.get();
            conversation.getMessages().add(message);
        }
        Conversation saved = conversationRepository.save(conversation);

        message.setConversation(saved);
        messageRepository.save(message);
    }

    private Message toEntity(MessageDto messageDto) {
        Message message = new Message();
        message.setContent(messageDto.getContent());
        return message;
    }

    private ConversationDto toDto(Conversation conversation) {
        return new ConversationDto(
                toDto(conversation.getRecipient()),
                toDto(conversation.getSender()),
                conversation.getMessages().stream()
                        .map(this::toDto)
                        .map(this::substitute)
                        .collect(Collectors.toList()));
    }

    private ContactDto toDto(Contact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhoneNumber());
    }

    private MessageDto toDto(Message message) {
        return new MessageDto(
                message.getRecipient().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getCreatedAt().toString());
    }

    private MessageDto substitute(MessageDto messageDto) {
        Contact recipient = contactRepository.getById(messageDto.getRecipientId());
        Contact sender = contactRepository.getById(messageDto.getSenderId());
        messageDto.setContent(
                templatingService.substitute(
                        messageDto.getContent(),
                        Map.of(
                                "recipient",
                                getFullName(recipient),
                                "sender",
                                getFullName(sender))));
        return messageDto;
    }

    private String getFullName(Contact contact) {
        return String.format("%s %s", contact.getFirstName(), contact.getLastName());
    }
}
