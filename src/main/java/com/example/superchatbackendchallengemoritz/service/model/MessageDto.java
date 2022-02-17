package com.example.superchatbackendchallengemoritz.service.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private UUID recipientId;
    private UUID senderId;
    private String content;
    private String timestamp;
}
