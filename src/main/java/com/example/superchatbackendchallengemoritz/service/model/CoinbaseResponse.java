package com.example.superchatbackendchallengemoritz.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinbaseResponse {
    private CoinbaseResponseData data;
}
