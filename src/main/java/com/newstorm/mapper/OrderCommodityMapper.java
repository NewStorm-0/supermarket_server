package com.newstorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.OrderCommodity;
import com.newstorm.service.OrderCommodityService;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCommodityMapper extends BaseMapper<OrderCommodity> {
}
