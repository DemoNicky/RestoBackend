package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.OrderDetailDto;
import com.dobudobu.resto.Dto.OrderDetailResponseDto;
import com.dobudobu.resto.Entity.*;
import com.dobudobu.resto.Repository.MenuRepository;
import com.dobudobu.resto.Repository.OrderDetailRepository;
import com.dobudobu.resto.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderDetailResponseDto createOrder(List<OrderDetailDto> orderDetailDto) {

        //mengecek apakah ada obejck yang duplikat atau tidak
        checkDuplicateObject(orderDetailDto);

//        orderDetailDtos.stream().collect(Collectors.groupingBy(Function.identity(),
//                Collectors.counting()))
//                .entrySet().stream().filter(e -> e.getKey().getMenuId() > 1L)
//                .map(e -> e.getKey())
//                .collect(Collectors.toList())
//                .forEach(orderDetailDto1 -> {
//                    throw new RuntimeException("duplicate object");
//                });

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDate(LocalDateTime.now());

        List<Order> orders = getOrders(orderDetailDto);
        Double x = getaDouble(orders);
        orderDetail.setPaymentStatus(OrderStatus.NOT_PAID);
        orderDetail.setPaymentTotal(x);
        orderDetail.setOrder(orders);

        orderDetailRepository.save(orderDetail);

        OrderDetailResponseDto orderDetailResponseDto = getOrderDetailResponseDto(orderDetail);
        return orderDetailResponseDto;
    }

    private OrderDetailResponseDto getOrderDetailResponseDto(OrderDetail orderDetail) {
        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto();
        orderDetailResponseDto.setQrQode(orderDetail.getId());
        return orderDetailResponseDto;
    }

    private void checkDuplicateObject(List<OrderDetailDto> orderDetailDto) {
        HashSet unique = new HashSet();
        for (OrderDetailDto s : orderDetailDto) {
            if (!unique.add(s.getMenuId())){
                throw new RuntimeException("duplicate object");
            }
        }
    }

    private Double getaDouble(List<Order> orders) {
        Double x = 0.0;
        for (Order order : orders) {
             Double z = order.getPayTotal();
             x += z;
        }
        return x;
    }

    private List<Order> getOrders(List<OrderDetailDto> orderDetailDto) {
        List<Order> orders = orderDetailDto.stream().map((p) -> {
            Order order = new Order();
            order.setOrderQuantity(p.getOrderQuantity());
            Menu menu = getMenu(p, order);
            Double payTotal = p.getOrderQuantity() * menu.getPrice();
            order.setPayTotal(payTotal);
            return order;
        }).collect(Collectors.toList());
        return orders;
    }

    private Menu getMenu(OrderDetailDto p, Order order) {
        Menu menu = menuRepository.findById(p.getMenuId()).orElseThrow(
                () -> new RuntimeException("Menu not found"));
        order.setMenu(menu);
        if (menu.getStatus().equals(false)){
            throw new RuntimeException("menu unaviable/menu belum tersedia");
        }
        return menu;
    }

    public List<OrderDetail> getAllData() {
        return orderDetailRepository.findAll();
    }
}
