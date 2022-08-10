package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.User;

public interface UserService extends IService<User> {
    User login(Integer account, String password);
}
