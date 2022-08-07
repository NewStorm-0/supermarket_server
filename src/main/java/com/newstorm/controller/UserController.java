package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NewStorm
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public JsonResult login(@RequestParam("account") Integer account,
                            @RequestParam("password") String password) {
        return userService.login(account, password);
    }
}
