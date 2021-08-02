package com.jak.payz.gateway.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private long paymentId;
    private String reference;
    private String cardHolder;
    private String cardNumber;
    private BigDecimal amount;

    private String paymentStatus;
    private int paymentStatusResponseCode;

}
