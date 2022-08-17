package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.UserOrder;
import com.newstorm.pojo.dto.OrderDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserOrderService extends IService<UserOrder> {
    double REWARD_POINTS_RETURN_RATE = 0.1;

    /**
     * 结账生成订单
     *
     * @param account  会员卡号
     * @param orderDTO 订单详情
     * @return 包含支付价格与赠送积分
     */
    Map<String, Object> checkout(Integer account, OrderDTO orderDTO);

    /**
     * 查询一段日期内的订单记录
     *
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 符号条件的订单记录
     */
    List<UserOrder> listBetweenDate(LocalDate startDate, LocalDate endDate);


}
