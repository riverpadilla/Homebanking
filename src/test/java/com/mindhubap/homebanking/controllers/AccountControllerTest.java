package com.mindhubap.homebanking.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void getAccounts() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/accounts",String.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAccount() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/accounts/1",String.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}