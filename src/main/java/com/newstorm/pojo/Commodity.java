package com.newstorm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("commodity")
@Data
public class Commodity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Double initialPrice;
    private Double silverPrice;
    private Double goldPrice;
    private Double platinumPrice;
    private Double specialPrice;
}
