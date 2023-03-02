package com.dobudobu.resto.Dto;

import com.dobudobu.resto.Entity.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentListResponseDto {

    private Double paymentTotal;

    private Double pay;

    private Double change;

    private String tableNumber;

    private String user;

    private OrderDetail orderDetail;

}
