package com.example.superchatbackendchallengemoritz.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookRequestDto {
    private ContactDto recipient;
    private ContactDto sender;
    private String message;
}
