package com.jak.payz.gateway.service;

import com.jak.payz.gateway.bankclient.BankClient;
import com.jak.payz.gateway.domain.Payment;
import com.jak.payz.gateway.mapper.PaymentMapper;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;
import com.jak.payz.gateway.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private BankClient mockClient;

    @Mock
    private PaymentRepository mockRepository;

    @Mock
    private PaymentMapper mockMapper;

    private PaymentServiceImpl classUnderTest;

    @BeforeEach
    void init() {
        classUnderTest = new PaymentServiceImpl(mockClient, mockRepository, mockMapper);
    }

    @Test
    void createPayment() {
        LocalDateTime now = LocalDateTime.now();
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder().reference("createtest").build();
        Payment mappedPayment = Payment.builder().reference("createtest").build();
        when(mockMapper.paymentFromPaymentRequestDTO(requestDTO)).thenReturn(mappedPayment);
        when(mockRepository.save(mappedPayment)).thenReturn(mappedPayment);
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(2, "a", "2-1", now, now);
        when(mockClient.sendPaymentRequest(requestDTO)).thenReturn(responseDTO);

        PaymentResponseDTO createdResponse = classUnderTest.createPayment(requestDTO);
        assertEquals(responseDTO, createdResponse);
        verify(mockRepository, times(1)).save(mappedPayment);
        verify(mockRepository, times(1)).flush();
        assertEquals(2, mappedPayment.getResponseCode());
        assertEquals("a", mappedPayment.getResponseMessage());
    }

    @Test
    void createPaymentWhenNoResponse() {
        LocalDateTime now = LocalDateTime.now();
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder().reference("createtest").build();
        Payment mappedPayment = Payment.builder().reference("createtest").build();
        when(mockMapper.paymentFromPaymentRequestDTO(requestDTO)).thenReturn(mappedPayment);
        when(mockRepository.save(mappedPayment)).thenReturn(mappedPayment);
        when(mockClient.sendPaymentRequest(requestDTO)).thenReturn(null);

        PaymentResponseDTO createdResponse = classUnderTest.createPayment(requestDTO);
        assertNotNull(createdResponse);
        assertEquals(-1, createdResponse.getResponseCode());
        assertEquals("Pending", createdResponse.getStatus());

        verify(mockRepository, times(1)).save(mappedPayment);
        verify(mockRepository, times(1)).flush();
        assertEquals(-1, mappedPayment.getResponseCode());
        assertEquals("Pending", mappedPayment.getResponseMessage());
    }

    @Test
    void getPaymentDetails() {
        PaymentDTO paymentDTO = PaymentDTO.builder().paymentId(4L).build();
        Payment mockPayment = Payment.builder().reference("get test").build();
        when(mockRepository.findById(4L)).thenReturn(Optional.ofNullable(mockPayment));
        when(mockMapper.paymentDTOFromPayment(mockPayment)).thenReturn(paymentDTO);
        PaymentDTO createdDTO = classUnderTest.getPaymentDetails(4L);
        assertEquals(paymentDTO, createdDTO);
        verify(mockRepository, times(1)).findById(4L);
        verify(mockMapper, times(1)).paymentDTOFromPayment(mockPayment);
    }

    @Test
    void getPayments() {
        Payment payment1 = Payment.builder().id(5L).reference("pay1").build();
        Payment payment2 = Payment.builder().id(6L).reference("pay2").build();
        when(mockRepository.findAll()).thenReturn(Arrays.asList(payment1, payment2));
        PaymentDTO paymentDTO1 = PaymentDTO.builder().paymentId(5L).reference("pay1").build();
        PaymentDTO paymentDTO2 = PaymentDTO.builder().paymentId(6L).reference("pay2").build();
        when(mockMapper.paymentDTOFromPayment(payment1)).thenReturn(paymentDTO1);
        when(mockMapper.paymentDTOFromPayment(payment2)).thenReturn(paymentDTO2);
        List<PaymentDTO> payments = classUnderTest.getPayments();
        assertEquals(2, payments.size());
        assertEquals(paymentDTO1, payments.get(0));
        assertEquals(paymentDTO2, payments.get(1));
    }
}