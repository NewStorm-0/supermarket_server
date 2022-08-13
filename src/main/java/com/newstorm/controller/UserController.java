package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.User;
import com.newstorm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NewStorm
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public JsonResult login(@RequestBody Map<String, Object> parameters) {
        try {
            parameters.forEach((k, v) -> log.info("Key: " + k + " Value: " + v));
            Map<String, Object> map = userService.login(
                    Integer.valueOf((String) parameters.get("account")),
                    (String) parameters.get("password"));
            return new JsonResult(map);
        } catch (NumberFormatException e) {
            throw new BaseException("输入信息格式错误");
        }

    }

    @PostMapping("/register")
    public JsonResult register(@RequestBody User user) {
        return new JsonResult(userService.register(user));
    }
}
