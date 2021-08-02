package com.jak.payz.gateway.mapper;

import org.springframework.stereotype.Component;

import com.jak.payz.gateway.domain.Payment;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;

import java.time.LocalDateTime;

@Component
public class PaymentMapper {

    public Payment paymentFromPaymentRequestDTO(PaymentRequestDTO dto) {
        return Payment.builder()
                .cardNumber(dto.getCardNumber().substring(0, 6) + "******" + dto.getCardNumber().substring(12))
                .cardHolder(dto.getCardHolderName())
                .reference(dto.getReference())
                .amount(dto.getAmount())
                .dateCreated(LocalDateTime.now())
                .dateModified(LocalDateTime.now())
                .build();
    }

    public PaymentDTO paymentDTOFromPayment(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getId())
                .cardHolder(payment.getCardHolder())
                .cardNumber(payment.getCardNumber())
                .paymentStatusResponseCode(payment.getResponseCode())
                .paymentStatus(payment.getResponseMessage())
                .reference(payment.getReference())
                .amount(payment.getAmount())
                .build();
    }

}
