package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.OrderDetailDto;
import com.dobudobu.resto.Entity.Menu;
import com.dobudobu.resto.Entity.Order;
import com.dobudobu.resto.Entity.OrderDetail;
import com.dobudobu.resto.Repository.MenuRepository;
import com.dobudobu.resto.Repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MenuRepository menuRepository;

    public void createOrder(List<OrderDetailDto> orderDetailDto) {

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDate(LocalDateTime.now());

        List<Order> orders = getOrders(orderDetailDto);
        Double x = getaDouble(orders);

        orderDetail.setPaymentTotal(x);
        orderDetail.setOrder(orders);

        orderDetailRepository.save(orderDetail);
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
            order.setUsername(p.getUsername());
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
