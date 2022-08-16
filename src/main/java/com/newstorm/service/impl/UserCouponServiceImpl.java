package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.UserCouponMapper;
import com.newstorm.pojo.UserCoupon;
import com.newstorm.service.UserCouponService;
import org.springframework.stereotype.Service;

@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
    @Override
    public boolean useCoupon(Integer account, Integer couponType) {
        UpdateWrapper<UserCoupon> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", account)
                .eq("coupon_type", couponType)
                .gt("quantity", 0)
                .setSql("quantity = quantity - 1");
        return update(updateWrapper);
    }
}
