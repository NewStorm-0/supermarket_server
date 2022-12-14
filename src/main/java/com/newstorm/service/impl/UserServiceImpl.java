package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.common.HmacUtils;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.exception.SqlDataErrorException;
import com.newstorm.exception.UserNotFoundException;
import com.newstorm.mapper.UserMapper;
import com.newstorm.pojo.*;
import com.newstorm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    ChargeService chargeService;
    RedeemService redeemService;
    CouponService couponService;
    UserCouponService userCouponService;

    @Autowired
    public void setChargeService(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @Autowired
    public void setRedeemService(RedeemService redeemService) {
        this.redeemService = redeemService;
    }

    @Autowired
    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    @Autowired
    public void setUserCouponService(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    @Override
    public Map<String, Object> login(Integer account, String password) {
        if (!(StringUtils.isBlank(password) || account == null)) {
            String processedPassword;
            try {
                processedPassword = HmacUtils.encrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException(e.getMessage());
            }
            Map<String, Object> conditions = new HashMap<>(2);
            conditions.put("account", account);
            conditions.put("password", processedPassword);
            List<User> users = getBaseMapper().selectByMap(conditions);
            if (users.size() > 0) {
                users.forEach(System.out::println);
                if (users.size() > 1) {
                    log.warn("?????????????????????????????????????????????");
                    throw new SqlDataErrorException("?????????????????????");
                }
                User temp = users.get(0);
                temp.setPassword(null);
                String authorization = JwtUtils.getUserToken(temp);
                Map<String, Object> map = new HashMap<>(2);
                map.put("user", temp);
                map.put("authorization", authorization);
                return map;
            } else {
                throw new UserNotFoundException("?????????????????????");
            }
        }
        throw new BaseException("??????????????????????????????");
    }

    @Override
    public Map<String, Integer> register(User user) {
        try {
            user.setPassword(HmacUtils.encrypt(user.getPassword()));
            save(user);
            log.info("Register new user: " + user);
            Map<String, Integer> map = new HashMap<>(1);
            map.put("account", user.getAccount());
            return map;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public boolean changePassword(Integer account, String oldPassword, String newPassword) {
        try {
            oldPassword = HmacUtils.encrypt(oldPassword);
            newPassword = HmacUtils.encrypt(newPassword);
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account", account).eq("password", oldPassword);
            updateWrapper.set("password", newPassword);
            return update(updateWrapper);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public boolean recharge(Integer account, Double amount) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).select("balance");
        List<Map<String, Object>> list = listMaps(queryWrapper);
        Double balance = (Double) list.get(0).get("balance");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account", account).set("balance", amount + balance);
        if (update(updateWrapper)) {
            Charge charge = new Charge();
            charge.setUserId(account);
            charge.setAmount(amount);
            charge.setTime(LocalDateTime.now());
            return chargeService.save(charge);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean redeem(Integer account, Integer couponType, Integer number) {
        // ??????????????????
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).select("reward_points");
        Map<String, Object> map = getMap(queryWrapper);
        Integer rewardPoints = (Integer) map.get("reward_points");

        // ??????????????????????????????????????????????????????????????????????????????????????????
        QueryWrapper<Coupon> couponQueryWrapper = new QueryWrapper<>();
        couponQueryWrapper.eq("type", couponType).select("cost");
        map = couponService.getMap(couponQueryWrapper);
        Integer costRewardPoints = (Integer) map.get("cost") * number;

        // ????????????????????????????????????????????????
        if (rewardPoints >= costRewardPoints) {
            // ??????????????????????????? user_coupon ????????????????????????????????????????????????????????????????????????
            // ??????????????????????????????

            // ????????????
            UpdateWrapper<UserCoupon> userCouponUpdateWrapper = new UpdateWrapper<>();
            userCouponUpdateWrapper.eq("user_id", account)
                    .eq("coupon_type", couponType)
                    .setSql("quantity = quantity + " + number);
            if (!userCouponService.update(userCouponUpdateWrapper)) {
                // ???????????????????????? entity ?????????????????? entity
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setUserId(account);
                userCoupon.setCouponType(couponType);
                userCoupon.setQuantity(number);
                if (!userCouponService.save(userCoupon)) {
                    throw new BaseException("????????????????????????????????????????????????");
                }
            }
            // ?????? user ?????????????????????
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account", account)
                    .set("reward_points", rewardPoints - costRewardPoints);

            if (update(updateWrapper)) {
                // ??? redeem ????????????????????????
                Redeem redeem = new Redeem();
                redeem.setUserId(account);
                redeem.setCouponType(couponType);
                redeem.setNumber(number);
                redeem.setValue(costRewardPoints);
                redeem.setTime(LocalDateTime.now());
                if (redeemService.save(redeem)) {
                    return true;
                } else {
                    throw new BaseException("???????????????????????????????????????");
                }
            } else {
                throw new BaseException("???????????????????????????????????????");
            }
        }
        return false;
    }

    @Override
    public boolean deductBalance(Integer account, Double amount) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account", account).ge("balance", amount).setSql("balance = balance - " + amount);
        return update(updateWrapper);
    }

    @Override
    public boolean addRewardPoints(Integer account, Integer points) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account", account).setSql("reward_points = reward_points + " + points);
        return update(updateWrapper);
    }
}