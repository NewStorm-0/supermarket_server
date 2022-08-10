package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.UserCouponMapper;
import com.newstorm.pojo.UserCoupon;
import com.newstorm.service.UserCouponService;
import org.springframework.stereotype.Service;

@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
}
