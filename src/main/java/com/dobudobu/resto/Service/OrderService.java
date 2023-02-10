package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.OrderDto;
import com.dobudobu.resto.Entity.Menu;
import com.dobudobu.resto.Entity.Order;
import com.dobudobu.resto.Repository.MenuRepository;
import com.dobudobu.resto.Repository.OrderRepository;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    public void saveData(List<OrderDto> orderDto) {


        List<Order> orders = orderDto.stream().map((p) ->{
            Order order = new Order();
            order.setUsername(p.getUsername());
            order.setOrderQuantity(p.getOrderQuantity());

            Menu menu = menuRepository.findById(p.getMenuId()).orElseThrow(
                    () -> new RuntimeException("Menu Not Found"));
            Double total = p.getOrderQuantity() * menu.getPrice();
            order.setPayTotal(total);
            order.setMenu(menu);
            Double x = order.getOrderQuantity() * menu.getPrice();
            order.setPayTotal(x);
            return order;
        }).collect(Collectors.toList());

        orderRepository.saveAll(orders);
    }
}
