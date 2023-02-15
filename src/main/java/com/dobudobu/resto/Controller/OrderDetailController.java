package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.OrderDetailDto;
import com.dobudobu.resto.Entity.OrderDetail;
import com.dobudobu.resto.Service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orderdetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<Void> insertOrder(@RequestBody List<OrderDetailDto> orderDetailDto){
        orderDetailService.createOrder(orderDetailDto);
        return ResponseEntity.created(URI.create("created")).build();
    }

    @GetMapping
    public List<OrderDetail> getData(){
        return orderDetailService.getAllData();
    }
}
