package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.UserOrder;
import com.newstorm.pojo.dto.OrderDTO;

import java.util.Map;

public interface UserOrderService extends IService<UserOrder> {
    double REWARD_POINTS_RETURN_RATE = 0.1;
    Map<String, Object> checkout(Integer account, OrderDTO orderDTO);
}
