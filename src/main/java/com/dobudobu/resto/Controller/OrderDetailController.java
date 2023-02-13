package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.OrderDetailDto;
import com.dobudobu.resto.Service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orderdetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<Void> insertOrder(@RequestBody OrderDetailDto orderDetailDto){
        orderDetailService.createOrder(orderDetailDto);
        return ResponseEntity.created(URI.create("created")).build();
    }

}
