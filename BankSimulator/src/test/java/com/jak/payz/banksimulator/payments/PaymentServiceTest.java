package com.jak.payz.banksimulator.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PaymentServiceTest {

    private final PaymentService paymentService = new PaymentService();

    @Test
    void testCardNumberEnding9999IsBlacklisted() {
        Response paymentResponseFor4444 = paymentService.processPayment(
                new Payment("P1", "P1Add", "PC1", "5555555555554444", "12/23", "123", new BigDecimal("2.21")));
        assertEquals(40103, paymentResponseFor4444.getResponseCode());
        assertEquals("Blacklisted details", paymentResponseFor4444.getStatus());
        assertNotNull(paymentResponseFor4444.getResponseId());
    }

    @Test
    void testCardNumberEnding1117IsFraudulent() {
        Response paymentResponseFor1117 = paymentService.processPayment(
                new Payment("P2", "P2Add", "PC2", "6011111111111117", "11/22", "111", new BigDecimal("33.11")));
        assertEquals(20059, paymentResponseFor1117.getResponseCode());
        assertEquals("Suspected fraud", paymentResponseFor1117.getStatus());
        assertNotNull(paymentResponseFor1117.getResponseId());
    }

    @Test
    void testCardNumberEnding0004IsNull() {
        Response paymentResponseFor0004 = paymentService.processPayment(
                new Payment("P3", "P3Add", "PC3", "3056930009020004", "16/66", "545", new BigDecimal("66.66")));
        assertNull(paymentResponseFor0004);
    }

    @Test
    void testOtherCardNumberIsApproved() {
        Response paymentResponse = paymentService.processPayment(
                new Payment("P4", "P4Add", "PC4", "4512234554323344", "09/24", "232", new BigDecimal("107.00")));
        assertEquals(10000, paymentResponse.getResponseCode());
        assertEquals("Approved", paymentResponse.getStatus());
        assertNotNull(paymentResponse.getResponseId());
    }
}
