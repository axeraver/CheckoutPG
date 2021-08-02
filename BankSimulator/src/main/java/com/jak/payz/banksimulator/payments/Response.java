package com.jak.payz.banksimulator.payments;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Response {

    int responseCode;
    String status;
    String responseId;
    LocalDateTime receivedDate;
    LocalDateTime processedDate;

    public static Response from(int responseCode, String status, String responseId, LocalDateTime receivedDate) {
        return Response.builder()
                .responseCode(responseCode)
                .status(status)
                .responseId(responseId)
                .receivedDate(receivedDate)
                .processedDate(LocalDateTime.now())
                .build();
    }

}
