package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.Administrator;

public interface AdministratorService extends IService<Administrator> {
    /**
     * 管理员登录
     *
     * @param account  管理员账号
     * @param password 管理员密码
     * @return token
     */
    String login(String account, String password);
}
