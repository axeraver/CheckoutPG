package com.jak.payz.banksimulator.payments;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public Response processPayment(final Payment payment) {
        final LocalDateTime receivedDate = LocalDateTime.now();
        final String responseId = generateResponseId();
        if(payment.getCardNumber().endsWith("4444")) {
            return Response.from(40103, "Blacklisted details", responseId, receivedDate);
        }
        if(payment.getCardNumber().endsWith("1117")) {
            return Response.from(20059, "Suspected fraud", responseId, receivedDate);
        }
        if(payment.getCardNumber().endsWith("0004")) {
            return null;
        }
        return Response.from(10000, "Approved", responseId, receivedDate);
    }

    private String generateResponseId() {
        return RandomStringGenerator.generate();
    }

}
