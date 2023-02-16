package com.dobudobu.resto.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

    @NotNull
    private String orderDetailId;

    @NotNull
    private Double pay;

}
