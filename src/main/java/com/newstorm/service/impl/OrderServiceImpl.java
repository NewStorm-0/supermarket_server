package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.OrderMapper;
import com.newstorm.pojo.Order;
import com.newstorm.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
