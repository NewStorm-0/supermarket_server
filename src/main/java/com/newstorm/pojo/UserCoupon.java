package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_coupon")
@Data
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer couponType;
    private Integer quantity;
}
