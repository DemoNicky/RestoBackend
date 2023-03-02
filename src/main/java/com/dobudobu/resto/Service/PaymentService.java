package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.PaymentDto;
import com.dobudobu.resto.Dto.PaymentListResponseDto;
import com.dobudobu.resto.Dto.PaymentResponseDto;
import com.dobudobu.resto.Entity.*;
import com.dobudobu.resto.Repository.OrderDetailRepository;
import com.dobudobu.resto.Repository.PaymentRepository;
import com.dobudobu.resto.Repository.TableRepository;
import com.dobudobu.resto.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private UserRepository userRepository;

    public PaymentResponseDto paymentOrder(PaymentDto paymentDto){


        //get table obejct
        Tables tablesdes = getTables();

        //get order detail object
        OrderDetail orderDetail = getOrderDetail(paymentDto);

        //get user
        User user = getUser();

        //count logic
        Double change = paymentCount(paymentDto, orderDetail);

        //json response
        PaymentResponseDto paymentResponseDto =
                getPaymentResponseDto(paymentDto, tablesdes, orderDetail, change, user.getFirstName());

        //payment object
        Payment payment = new Payment();
        payment.setPaymentTotal(orderDetail.getPaymentTotal());
        payment.setPay(paymentDto.getPay());
        payment.setChange(change);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTables(tablesdes);
        payment.setOrderStatus(OrderStatus.PAID);
        payment.setOrderDetail(orderDetail);
        payment.getOrderDetail().setPaymentStatus(OrderStatus.PAID);
        payment.setUsers(user);

        paymentRepository.save(payment);
        return paymentResponseDto;
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));
        return user;
    }

    private PaymentResponseDto getPaymentResponseDto(PaymentDto paymentDto, Tables tablesdes,
                                                     OrderDetail orderDetail, Double change, String user) {
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .paymentTotal(orderDetail.getPaymentTotal())
                .pay(paymentDto.getPay())
                .change(change)
                .tableNumber(tablesdes.getTableNumber())
                .orderDetail(orderDetail)
                .user(user)
                .build();
        return paymentResponseDto;
    }

    private OrderDetail getOrderDetail(PaymentDto paymentDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(paymentDto.getOrderDetailId()).orElseThrow(
                () -> new RuntimeException("orders id not found"));
        if (orderDetail.getPaymentStatus().equals(OrderStatus.PAID)){
            throw new RuntimeException("request di batalkan karna barang sudah di bayar");
        }
        return orderDetail;
    }

    private Double paymentCount(PaymentDto paymentDto, OrderDetail orderDetail) {
        if (paymentDto.getPay() <= orderDetail.getPaymentTotal()){
            throw new RuntimeException("Pembayaran/Saldo tidak cukup!!!");
        }
        Double change = paymentDto.getPay() - orderDetail.getPaymentTotal();
        return change;
    }

    private Tables getTables() {
        List<Tables> tables = tableRepository.findByStatusFalse();
        if (tables.equals(null)){
            throw new RuntimeException("Table not available/Meja tidak tersedia");
        }
        Random random = new Random();
        Tables tablesdes = tables.get(random.nextInt(tables.size()));
        return tablesdes;
    }

    public List<PaymentListResponseDto> getAllPaymentData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));

        List<Payment> payments = paymentRepository.findUser(user.getId());

        List<PaymentListResponseDto> paymentResponseDtos = payments.stream().map((p) -> {
            PaymentListResponseDto paymentResponse = new PaymentListResponseDto();
            paymentResponse.setPaymentTotal(p.getPaymentTotal());
            paymentResponse.setPay(p.getPay());
            paymentResponse.setChange(p.getChange());
            paymentResponse.setTableNumber(p.getTables().getTableNumber());
            paymentResponse.setUser(user.getFirstName());
            paymentResponse.setOrderDetail(p.getOrderDetail());

            return paymentResponse;
        }).collect(Collectors.toList());

        return paymentResponseDtos;
    }
}