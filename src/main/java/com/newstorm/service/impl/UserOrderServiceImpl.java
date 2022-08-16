package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.exception.BaseException;
import com.newstorm.mapper.UserOrderMapper;
import com.newstorm.pojo.*;
import com.newstorm.pojo.dto.CommodityDTO;
import com.newstorm.pojo.dto.OrderDTO;
import com.newstorm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {

    CommodityService commodityService;
    UserService userService;
    CouponService couponService;
    UserCouponService userCouponService;
    MembershipLevelService membershipLevelService;
    OrderCommodityService orderCommodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    @Autowired
    public void setUserCouponService(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    @Autowired
    public void setMembershipLevelService(MembershipLevelService membershipLevelService) {
        this.membershipLevelService = membershipLevelService;
    }

    @Autowired
    public void setOrderCommodityService(OrderCommodityService orderCommodityService) {
        this.orderCommodityService = orderCommodityService;
    }

    @Override
    @Transactional
    public Map<String, Object> checkout(Integer account, OrderDTO orderDTO) {
        double finalPrice;
        String couponInfo = null;
        // 计算订单商品的总初始价格
        double price = 0.00D;
        Map<Integer, Double> priceMap =  // 后续向 order_commodity 表插入数据需要用到
                new HashMap<>(orderDTO.getCommodityDTOList().size());
        for (CommodityDTO commodityDTO : orderDTO.getCommodityDTOList()) {
            double commodityPrice = commodityService.getPrice(commodityDTO.getCommodityId());
            priceMap.put(commodityDTO.getCommodityId(), commodityPrice);
            price += commodityPrice * commodityDTO.getNumber();
        }
        // 计算订单返还的积分
        int points = (int) (price * REWARD_POINTS_RETURN_RATE);
        // 查询会员信息
        User user = userService.getById(account);
        // 查询会员对应的折扣
        double discount = membershipLevelService.getDiscount(user.getLevel());

        if (orderDTO.getCouponType() != null) {
            // 若使用满减券，查询使用的满减券
            Coupon coupon = couponService.getById(orderDTO.getCouponType());
            if (price > coupon.getLowestAmount()) {
                // 可以使用满减券
                finalPrice = price * discount - coupon.getReliefAmount();
                // 减少会员对应的满减券
                if (!userCouponService.useCoupon(account, coupon.getType())) {
                    throw new BaseException("结算失败，会员不持有该满减券");
                }
                couponInfo = coupon.getLowestAmount() + "/" + coupon.getReliefAmount();
            } else {
                throw new BaseException("结算失败，未能达成使用满减券条件");
            }
        } else {
            finalPrice = price * discount;
        }
        // 防止最终价格变为负数
        finalPrice = finalPrice >= 0 ? finalPrice : 0;
        // 减少会员余额
        if (!userService.deductBalance(account, finalPrice)) {
            throw new BaseException("结算失败，会员卡内余额不足");
        }
        // 赠送会员积分
        if (!userService.addRewardPoints(account, points)) {
            throw new BaseException("结算失败，赠送会员积分失败");
        }
        // 插入 order 表信息
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(account);
        userOrder.setRewardPoints(points);
        userOrder.setTime(LocalDateTime.now());
        userOrder.setCouponInfo(couponInfo);
        if (!save(userOrder)) {
            throw new BaseException("结算失败，订单记录存储失败");
        }
        // 插入 order_commodity 表信息
        List<OrderCommodity> orderCommodityList = new ArrayList<>();
        for (CommodityDTO commodityDTO : orderDTO.getCommodityDTOList()) {
            OrderCommodity orderCommodity = new OrderCommodity();
            orderCommodity.setOrderId(userOrder.getId());
            orderCommodity.setCommodityId(commodityDTO.getCommodityId());
            orderCommodity.setQuantity(commodityDTO.getNumber());
            orderCommodity.setOriginalPrice(priceMap.get(commodityDTO.getCommodityId()));
            orderCommodity.setActualPrice(priceMap.get(commodityDTO.getCommodityId()) * discount);
            orderCommodityList.add(orderCommodity);
        }
        if (!orderCommodityService.saveBatch(orderCommodityList)) {
            throw new BaseException("结算失败，订单商品记录存储失败");
        }
        Map<String, Object> returnMap = new HashMap<>(2);
        returnMap.put("finalPrice", finalPrice);
        returnMap.put("points", points);
        return returnMap;
    }
}
