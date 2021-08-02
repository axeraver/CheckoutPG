package com.jak.payz.gateway.bankclient;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class BankClientTest {

    private BankClient bankClient;

    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialise() {
        String baseUrl = String.format("http://localhost:%d", mockWebServer.getPort());
        bankClient = new BankClient(baseUrl, "/bank/payments");
    }

    @Test
    void testCreatePayment() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"responseCode\": 1234, \"status\": \"good to go\", \"responseId\": \"randomestring\", \"receivedDate\": \"2021-02-02 12:23:23\", \"processedDate\": \"2021-02-02 12:23:25\"}")
                .addHeader("Content-Type", "application/json"));

        PaymentRequestDTO mockPaymentRequest = PaymentRequestDTO.builder()
                .reference("junit")
                .amount(new BigDecimal("1.33"))
                .cardHolderAddress("test street")
                .cardHolderName("tester")
                .cardNumber("1234123412341234")
                .cardHolderPostcode("A122BB")
                .cvv("123")
                .expiryDate("02/24")
                .build();

        PaymentResponseDTO response = bankClient.sendPaymentRequest(mockPaymentRequest);
        assertEquals(1234, response.getResponseCode());
    }

}