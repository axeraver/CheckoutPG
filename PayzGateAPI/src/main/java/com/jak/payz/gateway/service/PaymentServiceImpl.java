package com.jak.payz.gateway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jak.payz.gateway.PaymentNotFoundException;
import com.jak.payz.gateway.bankclient.BankClient;
import com.jak.payz.gateway.domain.Payment;
import com.jak.payz.gateway.mapper.PaymentMapper;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;
import com.jak.payz.gateway.repository.PaymentRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BankClient bankClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(BankClient bankClient, PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.bankClient = bankClient;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {
        log.info("Creating payment for request: {}", requestDTO);
        Payment payment = savePaymentRequest(requestDTO);
        PaymentResponseDTO responseDTO = getResponseFromBank(requestDTO);
        savePaymentResponse(payment, responseDTO);
        return responseDTO;
    }

    private PaymentResponseDTO getResponseFromBank(PaymentRequestDTO requestDTO) {
        PaymentResponseDTO responseDTO = null;
        try {
            log.info("Sending request to bank");
            responseDTO = bankClient.sendPaymentRequest(requestDTO);
        } catch (Exception e) {
            log.error("Transaction with bank failed: {}", e.getMessage());
        }
        if(responseDTO == null) {
            log.info("No response from bank");
            responseDTO = getPendingResponse();
        }
        return responseDTO;
    }

    private PaymentResponseDTO getPendingResponse() {
        return new PaymentResponseDTO(-1, "Pending", null, null, null);
    }

    private Payment savePaymentRequest(PaymentRequestDTO paymentRequestDTO) {
        log.info("Persisting payment request");
        Payment payment = paymentMapper.paymentFromPaymentRequestDTO(paymentRequestDTO);
        return paymentRepository.save(payment);
    }

    private void savePaymentResponse(Payment payment, PaymentResponseDTO responseDTO) {
        payment.setResponseCode(responseDTO.getResponseCode());
        payment.setResponseMessage(responseDTO.getStatus());
        log.info("Persisting payment response: {}", payment);
        paymentRepository.flush();
    }

    @Override
    public PaymentDTO getPaymentDetails(long paymentId) {
        log.info("Looking for payment info with id = {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);
        return paymentMapper.paymentDTOFromPayment(payment);
    }

    @Override
    public List<PaymentDTO> getPayments() {
        log.info("Fetching all payments");
        return paymentRepository.findAll().stream()
                .map(paymentMapper::paymentDTOFromPayment)
                .collect(Collectors.toList());
    }

}
