package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.PaymentDto;
import com.dobudobu.resto.Dto.PaymentListResponseDto;
import com.dobudobu.resto.Dto.PaymentResponseDto;
import com.dobudobu.resto.Entity.Payment;
import com.dobudobu.resto.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> payOrder(@RequestBody PaymentDto paymentDto){
        PaymentResponseDto paymentResponseDto = paymentService.paymentOrder(paymentDto);
        return ResponseEntity.ok(paymentResponseDto);
    }

    @GetMapping
    public List<PaymentListResponseDto> getAll(){
        List<PaymentListResponseDto> payments = paymentService.getAllPaymentData();
        return payments;
    }

}