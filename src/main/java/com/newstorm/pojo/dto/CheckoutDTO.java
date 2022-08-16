package com.newstorm.pojo.dto;

import lombok.Data;

@Data
public class CheckoutDTO {
    private Integer account;
    private OrderDTO order;
}
