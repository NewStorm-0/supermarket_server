package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.time.LocalDate;
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
        // ????????????????????????????????????
        double price = 0.00D;
        Map<Integer, Double> priceMap =  // ????????? order_commodity ???????????????????????????
                new HashMap<>(orderDTO.getCommodityDTOList().size());
        for (CommodityDTO commodityDTO : orderDTO.getCommodityDTOList()) {
            double commodityPrice = commodityService.getPrice(commodityDTO.getCommodityId());
            priceMap.put(commodityDTO.getCommodityId(), commodityPrice);
            price += commodityPrice * commodityDTO.getNumber();
        }
        // ???????????????????????????
        int points = (int) (price * REWARD_POINTS_RETURN_RATE);
        // ??????????????????
        User user = userService.getById(account);
        // ???????????????????????????
        double discount = membershipLevelService.getDiscount(user.getLevel());

        if (orderDTO.getCouponType() != null) {
            // ?????????????????????????????????????????????
            Coupon coupon = couponService.getById(orderDTO.getCouponType());
            if (price > coupon.getLowestAmount()) {
                // ?????????????????????
                finalPrice = price * discount - coupon.getReliefAmount();
                // ??????????????????????????????
                if (!userCouponService.useCoupon(account, coupon.getType())) {
                    throw new BaseException("??????????????????????????????????????????");
                }
                couponInfo = coupon.getLowestAmount() + "/" + coupon.getReliefAmount();
            } else {
                throw new BaseException("????????????????????????????????????????????????");
            }
        } else {
            finalPrice = price * discount;
        }
        // ??????????????????????????????
        finalPrice = finalPrice >= 0 ? finalPrice : 0;
        // ??????????????????
        if (!userService.deductBalance(account, finalPrice)) {
            throw new BaseException("???????????????????????????????????????");
        }
        // ??????????????????
        if (!userService.addRewardPoints(account, points)) {
            throw new BaseException("???????????????????????????????????????");
        }
        // ?????? order ?????????
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(account);
        userOrder.setPaymentAmount(finalPrice);
        userOrder.setRewardPoints(points);
        userOrder.setTime(LocalDateTime.now());
        userOrder.setCouponInfo(couponInfo);
        if (!save(userOrder)) {
            throw new BaseException("???????????????????????????????????????");
        }
        // ?????? order_commodity ?????????
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
            throw new BaseException("?????????????????????????????????????????????");
        }
        Map<String, Object> returnMap = new HashMap<>(2);
        returnMap.put("finalPrice", finalPrice);
        returnMap.put("points", points);
        return returnMap;
    }

    @Override
    public List<UserOrder> listBetweenDate(LocalDate startDate, LocalDate endDate) {
        QueryWrapper<UserOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", startDate, endDate);
        return list(queryWrapper);
    }
}
