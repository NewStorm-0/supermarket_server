package com.newstorm.pojo.dto;

import lombok.Data;

@Data
public class CheckoutDto {
    private Integer account;
    private OrderDto order;
}
