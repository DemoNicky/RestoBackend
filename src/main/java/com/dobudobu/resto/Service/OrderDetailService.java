package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.OrderDetailDto;
import com.dobudobu.resto.Repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void createOrder(OrderDetailDto orderDetailDto) {

    }
}
