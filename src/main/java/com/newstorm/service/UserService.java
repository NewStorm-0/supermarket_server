package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.User;

import java.util.Map;

public interface UserService extends IService<User> {

    Map<String, Object> login(Integer account, String password);
    Map<String, Integer> register(User user);
    boolean changePassword(Integer account, String oldPassword, String newPassword);
    boolean recharge(Integer account, Double amount);
    boolean redeem(Integer account, Integer couponType, Integer number);
}
