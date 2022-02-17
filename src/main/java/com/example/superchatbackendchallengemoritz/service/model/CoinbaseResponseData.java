package com.example.superchatbackendchallengemoritz.service.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinbaseResponseData {
    private String currency;
    private Map<String, String> rates;
}
