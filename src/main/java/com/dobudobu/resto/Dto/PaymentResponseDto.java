package com.dobudobu.resto.Dto;

import com.dobudobu.resto.Entity.OrderDetail;
import com.dobudobu.resto.Entity.User;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
public class PaymentResponseDto {

    private Double paymentTotal;

    private Double pay;

    private Double change;

    private String tableNumber;

    private String user;

    private OrderDetail orderDetail;

}
