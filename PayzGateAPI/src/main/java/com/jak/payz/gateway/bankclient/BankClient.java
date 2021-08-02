package com.jak.payz.gateway.bankclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;

import reactor.core.publisher.Mono;

@Service
public class BankClient {

    private final WebClient webClient;
    private final String endpoint;

    @Autowired
    public BankClient(@Value("${bank.base.url}") String baseUrl, @Value("${bank.payment.endpoint}") String endpoint) {
        webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(baseUrl)
                .build();
        this.endpoint = endpoint;
    }

    public PaymentResponseDTO sendPaymentRequest(PaymentRequestDTO paymentRequest) {
        Mono<PaymentResponseDTO> result = webClient
                .post()
                .uri(endpoint)
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class);
        return result.block();
    }

}
