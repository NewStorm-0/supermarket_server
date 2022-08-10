package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@TableName("Redeem")
@Data
public class Redeem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer value;
    private Integer couponType;
    private Integer number;
    private Integer userId;
    private Date time;
}
