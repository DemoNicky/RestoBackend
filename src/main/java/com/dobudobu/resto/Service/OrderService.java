package com.dobudobu.resto.Service;

import aj.org.objectweb.asm.TypeReference;
import com.dobudobu.resto.Dto.OrderDto;
import com.dobudobu.resto.Entity.Menu;
import com.dobudobu.resto.Entity.Order;
import com.dobudobu.resto.Repository.MenuRepository;
import com.dobudobu.resto.Repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    private final ObjectMapper objectMapper;

    public OrderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void saveData(List<OrderDto> orderDto) {

        List<Order> orders = orderDto.stream().map((p) ->{

            Order order = new Order();
            order.setOrderQuantity(p.getOrderQuantity());

            Menu menu = menuRepository.findById(p.getMenuId()).orElseThrow(
                    () -> new RuntimeException("Menu Not Found"));
            Double total = p.getOrderQuantity() * menu.getPrice();
            order.setPayTotal(total);

            order.setMenu(menu);
            return order;
        }).collect(Collectors.toList());

        orderRepository.saveAll(orders);
    }
}
