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
                    log.warn("登录异常：查询到的账户大于一个");
                    throw new SqlDataErrorException("后台数据库错误");
                }
                User temp = users.get(0);
                temp.setPassword(null);
                String authorization = JwtUtils.getToken(temp);
                Map<String, Object> map = new HashMap<>(2);
                map.put("user", temp);
                map.put("authorization", authorization);
                return map;
            }
        }
        throw new UserNotFoundException("卡号或密码错误");
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
            updateWrapper.eq("account", account)
                    .eq("password", oldPassword);
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
        updateWrapper.eq("account", account)
                .set("balance", amount + balance);
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
        //查询会员积分
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account)
                .select("reward_points");
        Map<String, Object> map = getMap(queryWrapper);
        Integer rewardPoints = (Integer) map.get("reward_points");

        //查询兑换的满减券所需要的积分并且根据数量计算出总共需要的积分
        QueryWrapper<Coupon> couponQueryWrapper = new QueryWrapper<>();
        couponQueryWrapper.eq("type", couponType)
                .select("cost");
        map = couponService.getMap(couponQueryWrapper);
        Integer costRewardPoints = (Integer) map.get("cost") * number;

        //判断会员的积分是否足够兑换满减券
        if (rewardPoints >= costRewardPoints) {
            //当足够兑换时，先在 user_coupon 中查找会员对应满减券的数量，然后加上兑换的数量，
            // 若没有记录则新建一条。 saveOrUpdate 方法可以方便的实现该功能

            //更新条件，saveOrUpdate(T entity, Wrapper<T> updateWrapper)
            UpdateWrapper<UserCoupon> userCouponUpdateWrapper = new UpdateWrapper<>();
            userCouponUpdateWrapper.eq("user_id", account)
                    .eq("coupon_type", couponType)
                    .setSql("quantity = quantity + " + number);
            //若更新失败，则将 entity 插入。以下为 entity
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserId(account);
            userCoupon.setCouponType(couponType);
            userCoupon.setQuantity(number);
            if (userCouponService.saveOrUpdate(userCoupon, userCouponUpdateWrapper)) {

                //减少 user 表中会员的积分
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("account", account)
                        .set("reward_points", rewardPoints - costRewardPoints);

                if (update(updateWrapper)) {

                    //向 redeem 表中插入兑换记录
                    Redeem redeem = new Redeem();
                    redeem.setUserId(account);
                    redeem.setCouponType(couponType);
                    redeem.setNumber(number);
                    redeem.setValue(costRewardPoints);
                    redeem.setTime(LocalDateTime.now());
                    return redeemService.save(redeem);
                }
            }
        }
        return false;
    }
}