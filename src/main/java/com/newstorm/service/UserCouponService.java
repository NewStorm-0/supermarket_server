package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.UserCoupon;

public interface UserCouponService extends IService<UserCoupon> {
    boolean useCoupon(Integer account, Integer couponType);
}
