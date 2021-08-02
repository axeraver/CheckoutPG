package com.jak.payz.banksimulator.payments;

import javax.validation.constraints.NotNull;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Payment {

    @NotNull String cardHolderName;
    @NotNull String cardHolderAddress;
    @NotNull String cardHolderPostcode;
    @NotNull String cardNumber;
    @NotNull String expiryDate;
    @NotNull String cvv;
    @NotNull BigDecimal amount;

}
