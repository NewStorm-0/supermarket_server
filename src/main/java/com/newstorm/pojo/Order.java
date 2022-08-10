package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@TableName("order")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer rewardPoints;
    private Date time;
    private String couponInfo;
}
