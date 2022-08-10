package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Coupon")
@Data
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Integer type;
    private Double reliefAmount;
    private Double lowestAmount;
    private Integer cost;
}
