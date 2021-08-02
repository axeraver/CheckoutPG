package com.jak.payz.gateway.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.jak.payz.gateway.PaymentNotFoundException;
import com.jak.payz.gateway.service.PaymentService;
import com.jak.payz.gateway.model.PaymentDTO;
import com.jak.payz.gateway.model.PaymentRequestDTO;
import com.jak.payz.gateway.model.PaymentResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    protected PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDTO createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        log.info("Processing payment");
        return paymentService.createPayment(paymentRequest);
    }

    @GetMapping("{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDTO getPaymentData(@PathVariable("paymentId") final long paymentId) {
        try {
            return paymentService.getPaymentDetails(paymentId);
        } catch (PaymentNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment [" + paymentId + "] was not found", ex);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentDTO> getPayments() {
        return paymentService.getPayments();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.put(
                ((FieldError) error).getField(),
                error.getDefaultMessage()
        ));
        return errors;
    }

}
