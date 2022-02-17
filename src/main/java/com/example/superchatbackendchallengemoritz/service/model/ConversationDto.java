package com.example.superchatbackendchallengemoritz.service.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private ContactDto recipient;
    private ContactDto sender;
    private List<MessageDto> messages;
}
