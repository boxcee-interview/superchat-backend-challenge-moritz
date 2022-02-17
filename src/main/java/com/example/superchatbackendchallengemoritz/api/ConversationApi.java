package com.example.superchatbackendchallengemoritz.api;

import com.example.superchatbackendchallengemoritz.api.model.Conversation;
import com.example.superchatbackendchallengemoritz.api.model.Message;
import com.example.superchatbackendchallengemoritz.service.ConversationService;
import com.example.superchatbackendchallengemoritz.service.model.ConversationDto;
import com.example.superchatbackendchallengemoritz.service.model.MessageDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationApi {

    private final ConversationService conversationService;

    @GetMapping(path = "/{contactId}", produces = "application/json")
    public ResponseEntity<?> getConversations(@PathVariable("contactId") UUID contactId) {
        List<ConversationDto> conversationDtos = conversationService.getConversations(contactId);
        return new ResponseEntity<>(toApi(conversationDtos), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        conversationService.createMessage(toDto(message));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private MessageDto toDto(Message message) {
        return new MessageDto(
                message.getRecipientId(), message.getSenderId(), message.getContent(), null);
    }

    private List<Conversation> toApi(List<ConversationDto> conversationDtos) {
        return conversationDtos.stream().map(this::toApi).collect(Collectors.toList());
    }

    private Conversation toApi(ConversationDto conversationDto) {
        return new Conversation(
                conversationDto.getRecipient().getId(),
                conversationDto.getSender().getId(),
                conversationDto.getMessages().stream()
                        .map(this::toApi)
                        .collect(Collectors.toList()));
    }

    private Message toApi(MessageDto messageDto) {
        return new Message(
                messageDto.getRecipientId(),
                messageDto.getSenderId(),
                messageDto.getContent(),
                messageDto.getTimestamp());
    }
}
