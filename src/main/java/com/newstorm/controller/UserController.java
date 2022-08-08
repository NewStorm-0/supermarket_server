package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author NewStorm
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody Map<String, Object> parameters) {
        return userService.login((Integer) parameters.get("account"),
                (String) parameters.get("password"));
    }
}
