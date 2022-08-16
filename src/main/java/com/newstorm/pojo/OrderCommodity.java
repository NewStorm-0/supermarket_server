package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("order_commodity")
@Data
public class OrderCommodity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer commodityId;
    private Integer quantity;
    private Double originalPrice;
    private Double actualPrice;
}
