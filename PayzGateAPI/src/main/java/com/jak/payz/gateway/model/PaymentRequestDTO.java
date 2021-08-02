package com.jak.payz.gateway.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.jak.payz.gateway.validator.ExpiryDate;
import com.jak.payz.gateway.validator.PostCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PaymentRequestDTO {

    private String reference;

    @NotBlank(message = "Card holder name is required.")
    @Size(max = 255)
    private String cardHolderName;

    @NotBlank(message = "Card holder address is required.")
    @Size(max = 1000)
    private String cardHolderAddress;

    @PostCode
    private String cardHolderPostcode;

    @NotBlank(message = "Credit card number is required")
    @CreditCardNumber(message = "Valid credit card number is required.")
    private String cardNumber;

    @ExpiryDate
    private String expiryDate;

    @NotBlank(message = "Valid credit card security code (cvv) is required.")
    @Size(min = 3, max = 4, message = "Security code must be 3 or 4 digits.")
    @PositiveOrZero(message = "Security code must only contain numbers.")
    private String cvv;

    @NotNull(message = "Payment amount is required.")
    @DecimalMin(value = "0.00", inclusive = false, message = "Payment amount must be greater than 0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount;

}
