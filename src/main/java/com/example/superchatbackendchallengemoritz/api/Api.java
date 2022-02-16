package com.example.superchatbackendchallengemoritz.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

  @GetMapping("/api/v1/contacts")
  public ResponseEntity<?> listAllContacts() {
    return new ResponseEntity<>("asd", HttpStatus.OK);
  }
}
