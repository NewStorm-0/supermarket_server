package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@TableName("charge")
@Data
public class Charge {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Double amount;
    private Date time;
}
