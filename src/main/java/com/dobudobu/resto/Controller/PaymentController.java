package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.PaymentDto;
import com.dobudobu.resto.Dto.PaymentResponseDto;
import com.dobudobu.resto.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
