package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.OrderCommodityMapper;
import com.newstorm.pojo.OrderCommodity;
import com.newstorm.service.OrderCommodityService;
import org.springframework.stereotype.Service;

@Service
public class OrderCommodityServiceImpl extends ServiceImpl<OrderCommodityMapper, OrderCommodity> implements OrderCommodityService {
}
