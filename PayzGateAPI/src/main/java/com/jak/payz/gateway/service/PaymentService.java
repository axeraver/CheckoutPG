package com.jak.payz.gateway.service;

import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO);

    PaymentDTO getPaymentDetails(long paymentId);

    List<PaymentDTO> getPayments();
}
