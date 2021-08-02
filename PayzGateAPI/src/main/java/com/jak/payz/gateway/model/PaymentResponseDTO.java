package com.jak.payz.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PaymentResponseDTO {

    int responseCode;
    String status;
    String responseId;

    @JsonIgnore
    LocalDateTime receivedDate;

    @JsonIgnore
    LocalDateTime processedDate;

}
