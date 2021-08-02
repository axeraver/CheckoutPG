package com.jak.payz.gateway.mapper;

import com.jak.payz.gateway.domain.Payment;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private final PaymentMapper classUnderTest = new PaymentMapper();

    @Test
    void paymentFromPaymentRequestDTO() {
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder()
                .cardNumber("1234123412341234")
                .cardHolderName("Frank Spencer")
                .cvv("123")
                .expiryDate("02/24")
                .cardHolderPostcode("A12AA")
                .cardHolderAddress("SMDAvenue")
                .amount(BigDecimal.TEN)
                .reference("mapper test")
                .build();

        Payment payment = classUnderTest.paymentFromPaymentRequestDTO(requestDTO);
        assertEquals("123412******1234", payment.getCardNumber());
        assertEquals("Frank Spencer", payment.getCardHolder());
        assertEquals("mapper test", payment.getReference());
        assertEquals(BigDecimal.TEN, payment.getAmount());
        assertTrue(payment.getDateCreated().isBefore(LocalDateTime.now().plusMinutes(1)));
        assertTrue(payment.getDateModified().isBefore(LocalDateTime.now().plusMinutes(1)));
    }

    @Test
    void paymentDTOFromPayment() {
        Payment payment = Payment.builder()
                .id(5L)
                .cardHolder("Betty Spencer")
                .cardNumber("543412******3232")
                .responseCode(10000)
                .responseMessage("Authorised")
                .reference("mapper test 2")
                .amount(BigDecimal.ONE)
                .build();

        PaymentDTO paymentDTO = classUnderTest.paymentDTOFromPayment(payment);
        assertEquals(5L, paymentDTO.getPaymentId());
        assertEquals("Betty Spencer", paymentDTO.getCardHolder());
        assertEquals("543412******3232", paymentDTO.getCardNumber());
        assertEquals(10000, paymentDTO.getPaymentStatusResponseCode());
        assertEquals("Authorised", paymentDTO.getPaymentStatus());
        assertEquals("mapper test 2", paymentDTO.getReference());
        assertEquals(BigDecimal.ONE, paymentDTO.getAmount());
    }

}