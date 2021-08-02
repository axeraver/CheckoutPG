package com.jak.payz.banksimulator.payments;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService mockPaymentService;

    @Test
    void testPaymentRequest() throws Exception {
        Payment payment = new Payment("John Smith", "Brewers Lane", "B3 3RS", "1111222233334444", "12/23", "123", new BigDecimal("12.23"));

        when(mockPaymentService.processPayment(payment)).thenReturn(new Response(1, "good", "unit", LocalDateTime.now(), LocalDateTime.now()));

        mockMvc.perform(
                post("/bank/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(payment))
                )
                .andExpect(status().isOk());

    }

}
