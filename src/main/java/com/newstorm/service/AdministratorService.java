package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.Administrator;
import com.newstorm.pojo.User;

public interface AdministratorService extends IService<Administrator> {
    /**
     * 管理员登录
     *
     * @param account  管理员账号
     * @param password 管理员密码
     * @return token
     */
    String login(String account, String password);

    /**
     * 修改会员信息
     *
     * @param user User实体
     * @return 是否修改成功
     */
    boolean changeUser(User user);

    /**
     * 检查管理员密码是否正确
     *
     * @param password 密码原数据
     * @return 密码是否正确
     */
    boolean checkAdministratorPassword(String account, String password);
}
