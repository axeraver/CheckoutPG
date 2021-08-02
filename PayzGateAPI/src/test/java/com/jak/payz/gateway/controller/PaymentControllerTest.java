package com.jak.payz.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jak.payz.gateway.PaymentNotFoundException;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;
import com.jak.payz.gateway.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    void postValidPayment() throws Exception {
        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .amount(new BigDecimal("1.20"))
                .cardNumber("4242424242424242")
                .expiryDate("12/34")
                .reference("junit test")
                .cardHolderAddress("test street")
                .cardHolderName("t ester")
                .cardHolderPostcode("ab12cd")
                .cvv("123")
                .build();

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(123, "good", "123-good", LocalDateTime.now(), LocalDateTime.now());
        when(paymentService.createPayment(paymentRequestDTO)).thenReturn(paymentResponseDTO);
        mockMvc.perform(
                post("/api/v1/payments")
                        .content(objectMapper.writeValueAsString(paymentRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void postInvalidPayment() throws Exception {
        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .amount(new BigDecimal("1.20"))
                .build();
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(666, "bad", "666-bad", LocalDateTime.now(), LocalDateTime.now());
        when(paymentService.createPayment(paymentRequestDTO)).thenReturn(paymentResponseDTO);
        mockMvc.perform(
                post("/api/v1/payments")
                        .content(objectMapper.writeValueAsString(paymentRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void getPaymentDataWithValidId() throws Exception {
        PaymentDTO payment = PaymentDTO.builder().paymentId(1L).paymentStatus("good").build();
        when(paymentService.getPaymentDetails(1L)).thenReturn(payment);
        mockMvc.perform(
                get("/api/v1/payments/1")
        ).andExpect(status().isOk());
    }

    @Test
    void getPaymentDataWithInvalidId() throws Exception {
        when(paymentService.getPaymentDetails(2L)).thenThrow(new PaymentNotFoundException());
        mockMvc.perform(
                get("/api/v1/payments/2")
        ).andExpect(status().isNotFound());
    }

    @Test
    void getAllPayments() throws Exception {
        when(paymentService.getPayments()).thenReturn(Collections.emptyList());
        mockMvc.perform(
                get("/api/v1/payments")
        ).andExpect(status().isOk());
    }

}