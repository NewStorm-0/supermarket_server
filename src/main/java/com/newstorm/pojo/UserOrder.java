package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("user_order")
@Data
public class UserOrder {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Double paymentAmount;
    private Integer rewardPoints;
    private LocalDateTime time;
    private String couponInfo;
}
