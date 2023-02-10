package com.dobudobu.resto.Dto;

import com.dobudobu.resto.Entity.Menu;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "username cant be null")
    private String username;

    @NotNull(message = "order Quantity cant be null")
    @Min(1)
    private int orderQuantity;

    @NotNull(message = "Menu cant be null")
    private Long menuId;

}
