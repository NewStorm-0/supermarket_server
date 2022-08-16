package com.newstorm.pojo.dto;

import com.newstorm.pojo.Coupon;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private List<CommodityDto> commodityDtoList;
    private Integer couponType;
}
