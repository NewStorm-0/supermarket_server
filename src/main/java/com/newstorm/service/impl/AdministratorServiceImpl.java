package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.common.HmacUtils;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.mapper.AdministratorMapper;
import com.newstorm.mapper.UserMapper;
import com.newstorm.pojo.Administrator;
import com.newstorm.pojo.MembershipLevel;
import com.newstorm.pojo.User;
import com.newstorm.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> implements AdministratorService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String login(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new BaseException("账号或密码不可为空白");
        }
        String processedPassword;
        try {
            processedPassword = HmacUtils.encrypt(password);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        Map<String, Object> conditions = new HashMap<>(2);
        conditions.put("account", account);
        conditions.put("password", processedPassword);
        List<Administrator> administratorList = listByMap(conditions);
        if (administratorList.size() > 0) {
            return JwtUtils.getAdministratorToken(administratorList.get(0));
        } else {
            throw new BaseException("账号或密码错误");
        }
    }

    @Override
    public boolean changeUser(User user) {
        try {
            user.setPassword(HmacUtils.encrypt(user.getPassword()));
            user.setBalance(null);
            user.setLevel(null);
            user.setRewardPoints(null);
            return userMapper.updateById(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public boolean checkAdministratorPassword(String account, String password) {
        try {
            String processedPassword = HmacUtils.encrypt(password);
            Map<String, Object> conditions = new HashMap<>(2);
            conditions.put("account", account);
            conditions.put("password", processedPassword);
            List<Administrator> administratorList = listByMap(conditions);
            return administratorList.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }
}
