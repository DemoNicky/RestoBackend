package com.dobudobu.resto.Dto;

import com.dobudobu.resto.Entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponseDto {

    private Double paymentTotal;

    private Double pay;

    private Double change;

    private String tableNumber;

    private OrderDetail orderDetail;

}
