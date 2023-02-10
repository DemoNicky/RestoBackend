package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.OrderDto;
import com.dobudobu.resto.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrderData(@RequestBody List<OrderDto> orderDto){
        orderService.saveData(orderDto);
        return ResponseEntity.created(URI.create("/orders")).build();
    }

}
