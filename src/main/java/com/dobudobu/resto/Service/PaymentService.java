package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.PaymentDto;
import com.dobudobu.resto.Dto.PaymentResponseDto;
import com.dobudobu.resto.Entity.OrderDetail;
import com.dobudobu.resto.Entity.OrderStatus;
import com.dobudobu.resto.Entity.Payment;
import com.dobudobu.resto.Entity.Tables;
import com.dobudobu.resto.Repository.OrderDetailRepository;
import com.dobudobu.resto.Repository.PaymentRepository;
import com.dobudobu.resto.Repository.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private TableRepository tableRepository;

    public PaymentResponseDto paymentOrder(PaymentDto paymentDto) {

        //get table obejct
        Tables tablesdes = getTables();

        //get order detail object
        OrderDetail orderDetail = getOrderDetail(paymentDto);

        //count logic
        Double change = paymentCount(paymentDto, orderDetail);

        //json response
        PaymentResponseDto paymentResponseDto =
                getPaymentResponseDto(paymentDto, tablesdes, orderDetail, change);

        //payment object
        Payment payment = new Payment();
        payment.setPaymentTotal(orderDetail.getPaymentTotal());
        payment.setPay(paymentDto.getPay());
        payment.setChange(change);
        payment.setTables(tablesdes);
        payment.setOrderStatus(OrderStatus.PAID);
        payment.getOrderDetail().setPaymentStatus(OrderStatus.PAID);
        payment.setOrderDetail(orderDetail);

        paymentRepository.save(payment);
        return paymentResponseDto;
    }

    private PaymentResponseDto getPaymentResponseDto(PaymentDto paymentDto, Tables tablesdes, OrderDetail orderDetail, Double change) {
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .paymentTotal(orderDetail.getPaymentTotal())
                .pay(paymentDto.getPay())
                .change(change)
                .tableNumber(tablesdes.getTableNumber())
                .orderDetail(orderDetail)
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
}
