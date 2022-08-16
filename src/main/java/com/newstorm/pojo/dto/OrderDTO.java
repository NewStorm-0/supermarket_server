package com.newstorm.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private List<CommodityDTO> commodityDTOList;
    private Integer couponType;
}
