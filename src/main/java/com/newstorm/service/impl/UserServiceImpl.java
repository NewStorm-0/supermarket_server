package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.common.HmacUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.exception.SqlDataErrorException;
import com.newstorm.exception.UserNotFoundException;
import com.newstorm.mapper.UserMapper;
import com.newstorm.pojo.User;
import com.newstorm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(Integer account, String password) {
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
                users.get(0).setPassword(null);
                return users.get(0);
            }
        }
        throw new UserNotFoundException("卡号或密码错误");
    }
}
