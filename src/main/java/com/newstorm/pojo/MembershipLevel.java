package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("membership_level")
@Data
public class MembershipLevel {
    @TableId(type = IdType.AUTO)
    private Integer type;
    private String name;
    private Double requiredAmount;
    private Double discount;
}
